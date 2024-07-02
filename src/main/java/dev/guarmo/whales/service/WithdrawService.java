package dev.guarmo.whales.service;

import dev.guarmo.whales.model.user.UserCredentials;
import dev.guarmo.whales.model.transaction.withdraw.MoneyWithdraw;
import dev.guarmo.whales.model.transaction.withdraw.WithdrawStatus;
import dev.guarmo.whales.model.transaction.withdraw.dto.GetWithdrawDto;
import dev.guarmo.whales.model.transaction.withdraw.dto.PostWithdrawDto;
import dev.guarmo.whales.model.transaction.withdraw.mapper.WithdrawMapper;
import dev.guarmo.whales.repository.UserCredentialsRepo;
import dev.guarmo.whales.repository.WithdrawRepo;
import dev.guarmo.whales.teleg.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WithdrawService {

    private final WithdrawRepo withdrawRepo;
    private final UserCredentialsRepo userCredentialsRepo;
    private final WithdrawMapper withdrawMapper;
    private final TelegramService telegramService;
    private final CryptoConvertService cryptoConvertService;

    @Transactional
    public GetWithdrawDto addWithdrawRequest(PostWithdrawDto postWithdrawDto, String login) {
        UserCredentials userCredentials = userCredentialsRepo.findByLogin(login).orElseThrow();
        if (userCredentials.getBalanceAmount() < postWithdrawDto.getTransactionAmount()) {
            throw new RuntimeException("Not enough balance");
        }

        MoneyWithdraw model = withdrawMapper.toModel(postWithdrawDto);
        model.setWithdrawStatus(WithdrawStatus.PENDING);
        MoneyWithdraw saved = withdrawRepo.save(model);

        userCredentials.getWithdraws().add(model);
        GetWithdrawDto withdrawGetDto = withdrawMapper.toGetDto(saved);

        Double priceInUsdPerCoin = cryptoConvertService.getCoinPriceInUsd(postWithdrawDto.getCurrency()).getPriceInUsd();
        // This transaction will be in native coin. User enters sum in dollars or coins he wants to withdraw
        withdrawGetDto.setTransactionAmount(withdrawGetDto.getTransactionAmount() / priceInUsdPerCoin);
        telegramService.sendNotificationAboutWithdraw(userCredentials, withdrawGetDto);

        userCredentials.setBalanceAmount(
                userCredentials.getBalanceAmount()
                - model.getTransactionAmount());
        userCredentialsRepo.save(userCredentials);

        return withdrawGetDto;
    }

    public List<GetWithdrawDto> getWithdrawsOfUserByLogin(String tgid) {
        UserCredentials userCredentials = userCredentialsRepo.findByLogin(tgid).orElseThrow();
        return userCredentials.getWithdraws().stream().map(withdrawMapper::toGetDto).toList();
    }

    public List<MoneyWithdraw> getWithdrawModelsByLogin(String tgid) {
        UserCredentials userCredentials = userCredentialsRepo.findByLogin(tgid).orElseThrow();
        return userCredentials.getWithdraws();
    }
}
