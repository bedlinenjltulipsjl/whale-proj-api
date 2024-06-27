package dev.guarmo.whales.service;

import dev.guarmo.whales.model.transaction.deposit.Deposit;
import dev.guarmo.whales.model.transaction.deposit.dto.GetDepositDto;
import dev.guarmo.whales.model.transaction.deposit.dto.PostDepositDto;
import dev.guarmo.whales.model.transaction.deposit.mapper.DepositMapper;
import dev.guarmo.whales.model.user.UserCredentials;
import dev.guarmo.whales.repository.DepositRepo;
import dev.guarmo.whales.repository.UserCredentialsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepositService {
    private final DepositRepo tranRepo;
    private final DepositMapper depositMapper;
    private final DepositRepo depositRepo;
    private final UserCredentialsRepo userCredentialsRepo;

    public GetDepositDto addTransactionToUser(MultiValueMap<String, String> formData) {
        // Take transaction values from data send to us from west wallet
        PostDepositDto postTranDto = depositMapper.toPostModel(formData);

        Deposit savedTran = tranRepo.save(depositMapper.toModel(postTranDto));

        String userTgIdIdentifierInLabel = postTranDto.getLabel();
        UserCredentials userCredentials = userCredentialsRepo.findByLogin(userTgIdIdentifierInLabel).orElseThrow();

        List<Deposit> transactions = userCredentials.getDeposits();
        transactions.add(savedTran);
        userCredentials.setDeposits(transactions);

        userCredentialsRepo.save(userCredentials);
        return depositMapper.toGetDto(savedTran);
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
}
