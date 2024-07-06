package dev.guarmo.whales.service;

import dev.guarmo.whales.helper.UserHelper;
import dev.guarmo.whales.model.investmodel.InvestModel;
import dev.guarmo.whales.model.investmodel.dto.GetInvestModel;
import dev.guarmo.whales.model.investmodel.mapper.InvestModelMapper;
import dev.guarmo.whales.model.transaction.PayTransaction;
import dev.guarmo.whales.model.transaction.income.IncomeType;
import dev.guarmo.whales.model.user.RoleStatus;
import dev.guarmo.whales.model.user.UserCredentials;
import dev.guarmo.whales.model.user.dto.*;
import dev.guarmo.whales.model.user.mapper.UserMapper;
import dev.guarmo.whales.repository.UserCredentialsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserCredentialsRepo repository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final InvestModelMapper investModelMapper;
    private final UserCredentialsRepo userCredentialsRepository;
    private final InvestModelService investModelService;
    private final UserHelper userHelper;
    private final AllTransactionService allTransactionService;

    public GetUserCredentialsDto addUser(PostUserDto user, RoleStatus role) {
        UserCredentials model = userMapper.toModel(user);
        model.setRole(role);
        model.setPassword(passwordEncoder.encode(user.getPassword()));

        List<InvestModel> generatedInvestModels =
                investModelService.generateDefaultInvestModels();
        model.setInvestModels(generatedInvestModels);
        model.setBalanceAmount(100);

        UserCredentials save = repository.save(model);
        UserCredentials upperReferral = model.getUpperReferral();
        upperReferral.getBottomReferrals().add(save);

        repository.save(upperReferral);
        return userMapper.toGetCredentialsDto(save);
    }

    public GetUserCredentialsDto getByCredentialsByLogin(String login) {
        UserCredentials userCredentials = repository.findByLogin(login).orElseThrow();
        return userMapper.toGetCredentialsDto(userCredentials);
    }

    public GetContentWithoutHistoryUserDto findByLogin(String tgid) {
        UserCredentials userCredentials = userCredentialsRepository.findByLogin(tgid).orElseThrow();
        return userMapper.toGetWithoutHistoryDto(userCredentials);
    }

    public GetFullDto getFourLevelsReferralTree(String tgid) {
        UserCredentials userCredentials = userCredentialsRepository.findByLogin(tgid).orElseThrow();
        return userMapper.toFullGetDto(userCredentials);
    }

    public GetFullDto findFullDtoByLogin(String name) {
        UserCredentials model = userHelper.findByLoginModel(name);
        GetFullDto fullGetDto = userMapper.toFullGetDto(model);
        fullGetDto.setTransactions(allTransactionService.getAllTypesOfTransactionsByUser(model));

        fullGetDto.setInvestModels(model
                .getInvestModels()
                .stream()
                .map(investModel -> {
                     GetInvestModel dto = investModelMapper.toGetDto(investModel);
                     dto.setMainBonusAmount(
                             model.getIncomes()
                                     .stream()
                                     .filter(income -> income
                                             .getIncomeType()
                                             .equals(IncomeType.MAIN)
                                             && income.getPurchasedModel()
                                             .equals(investModel.getDetails().getInvestModelLevel())
                                     )
                                     .mapToDouble(PayTransaction::getTransactionAmount)
                                     .sum()
                     );
                     dto.setPartnerBonusAmount(
                             model.getIncomes()
                                     .stream()
                                     .filter(income -> income
                                             .getIncomeType()
                                             .equals(IncomeType.REFERRAL)
                                             && income.getPurchasedModel()
                                             .equals(investModel.getDetails().getInvestModelLevel())
                                     )
                                     .mapToDouble(PayTransaction::getTransactionAmount)
                                     .sum()
                     );
                     return dto;
                })
                .toList());

        return fullGetDto;
    }

    public List<GetTopUserDto> getTopTen() {
        Pageable topTen = PageRequest.of(0, 12);
        Page<UserCredentials> resultPage = userCredentialsRepository.findTopByMaxSumOfIncomes(topTen);
        List<UserCredentials> content = resultPage.getContent();
        return content
                .stream()
                .map(userMapper::toGetTopUserDto)
                .toList();
    }
}
