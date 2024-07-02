package dev.guarmo.whales.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.guarmo.whales.helper.UserHelper;
import dev.guarmo.whales.model.currency.CryptoCurrency;
import dev.guarmo.whales.model.transaction.deposit.Deposit;
import dev.guarmo.whales.model.transaction.deposit.dto.GetDepositDto;
import dev.guarmo.whales.model.transaction.deposit.dto.PostDepositDto;
import dev.guarmo.whales.model.transaction.deposit.mapper.DepositMapper;
import dev.guarmo.whales.model.user.UserCredentials;
import dev.guarmo.whales.repository.DepositRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepositService {
    private final DepositRepo depositRepo;
    private final DepositMapper depositMapper;
    private final WestWalletService westWalletService;
    private final CryptoConvertService cryptoConvertService;
    private final UserHelper userHelper;

    // From west wallet post request we get transaction ID
    // Then we find transaction with this ID in west wallet DB and add it to user
    public GetDepositDto linkDepositToUser(long tranId) {
        String tranAsJson = westWalletService.getInfoForIncomeTransaction(tranId);
        try {
            PostDepositDto postTranDto = depositMapper.fromJsonToPost(tranAsJson);

            CryptoCurrency coinPriceInUsd = cryptoConvertService.getCoinPriceInUsd(postTranDto.getCurrency());
            postTranDto.setTransactionAmount(postTranDto.getTransactionAmount() * coinPriceInUsd.getPriceInUsd());

            return saveAndLinkDepositToUser(postTranDto);
        } catch (JsonProcessingException e) {
            log.error("Error when parsing returned transaction body as JSON: {}", e.getMessage());
            throw new RuntimeException("Error when parsing returned transaction body as JSON: {}", e);
        }
    }

    private UserCredentials saveAndLinkDepositToUser(PostDepositDto postTranDto) {
        Deposit savedTran = depositRepo.save(depositMapper.toModel(postTranDto));
        // User login saved as label in transaction
        String userLogin = postTranDto.getLabel();
        UserCredentials userCredentials = userHelper.findByLoginModel(userLogin);


        userCredentials.getDeposits().add(savedTran);
        userCredentials.setBalanceAmount(
                userCredentials.getBalanceAmount()
                + savedTran.getTransactionAmount());

        return depositMapper.toGetDto(savedTran);
    }
}
