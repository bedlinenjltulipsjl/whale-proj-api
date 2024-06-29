package dev.guarmo.whales.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.guarmo.whales.model.transaction.deposit.Deposit;
import dev.guarmo.whales.model.transaction.deposit.dto.GetDepositDto;
import dev.guarmo.whales.model.transaction.deposit.dto.PostDepositDto;
import dev.guarmo.whales.model.transaction.deposit.mapper.DepositMapper;
import dev.guarmo.whales.model.user.UserCredentials;
import dev.guarmo.whales.repository.DepositRepo;
import dev.guarmo.whales.repository.UserCredentialsRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepositService {
    private final DepositRepo tranRepo;
    private final DepositMapper depositMapper;
    private final DepositRepo depositRepo;
    private final UserCredentialsRepo userCredentialsRepo;
    private final WestWalletService westWalletService;

    public GetDepositDto addTransactionToUser(MultiValueMap<String, String> formData) {
        // Take transaction values from data send to us from west wallet
        PostDepositDto postTranDto = depositMapper.toPostModel(formData);
        return linkTransactionToUser(postTranDto);
    }

    public List<GetDepositDto> findAllByIds(List<Long> transactionIds) {
        return depositRepo.findAllById(transactionIds)
                .stream()
                .map(depositMapper::toGetDto)
                .toList();
    }

    public List<GetDepositDto> findAllTransactionsByLogin(String login) {
        UserCredentials userCredentials = userCredentialsRepo.findByLogin(login).orElseThrow();
        return userCredentials.getDeposits().stream().map(depositMapper::toGetDto).toList();
    }

    public List<Deposit> findAllDepositModelsByLogin(String login) {
        UserCredentials userCredentials = userCredentialsRepo.findByLogin(login).orElseThrow();
        return userCredentials.getDeposits();
    }

    public GetDepositDto addTransactionToUserByWestWalletIdId(long tranId) {
        String tranAsJson = westWalletService.getInfoForIncomeTransaction(tranId);
        try {
            PostDepositDto postTranDto = depositMapper.fromJsonToPost(tranAsJson);
            return linkTransactionToUser(postTranDto);
        } catch (JsonProcessingException e) {
            log.error("Error when parsing returned transaction body as JSON: {}", e.getMessage());
            throw new RuntimeException("Error when parsing returned transaction body as JSON: {}", e);
        }
    }

    private GetDepositDto linkTransactionToUser(PostDepositDto postTranDto) {
        Deposit savedTran = tranRepo.save(depositMapper.toModel(postTranDto));

        String userTgIdIdentifierInLabel = postTranDto.getLabel();
        UserCredentials userCredentials = userCredentialsRepo.findByLogin(userTgIdIdentifierInLabel).orElseThrow();

        userCredentials.getDeposits().add(savedTran);
        userCredentials.setBalanceAmount(
                userCredentials.getBalanceAmount()
                + savedTran.getTransactionAmount());

        userCredentialsRepo.save(userCredentials);
        return depositMapper.toGetDto(savedTran);
    }
}
