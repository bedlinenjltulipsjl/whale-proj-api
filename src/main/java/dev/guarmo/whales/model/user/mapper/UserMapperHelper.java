package dev.guarmo.whales.model.user.mapper;

import dev.guarmo.whales.helper.UserHelper;
import dev.guarmo.whales.model.transaction.GetTransaction;
import dev.guarmo.whales.model.transaction.PayTransaction;
import dev.guarmo.whales.model.transaction.deposit.Deposit;
import dev.guarmo.whales.model.transaction.income.Income;
import dev.guarmo.whales.model.user.UserCredentials;
import dev.guarmo.whales.model.user.dto.GetTopUserDto;
import dev.guarmo.whales.repository.UserCredentialsRepo;
import dev.guarmo.whales.service.AllTransactionService;
import dev.guarmo.whales.service.UserService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapperHelper {

    private final UserCredentialsRepo userCredentialsRepo;
    private final AllTransactionService allTransactionService;
    private final UserHelper userHelper;

    @Named("mapUpperReferralByLogin")
    public UserCredentials mapUpperReferralByLogin(String login) {
        return userCredentialsRepo.findByLogin(login).orElseThrow();
    }

    @Named("mapUpperReferralToLogin")
    public String mapUpperReferralByLogin(UserCredentials userCredentials) {
        return userCredentials.getLogin();
    }

    @Named("mapTransactionsToUser")
    public List<GetTransaction> getPurchaseDtoList(String login) {
        return allTransactionService.getAllTypesOfTransactions(login);
    }

    @Named("getBottomReferralsAmount")
    public int getInvestedAmount(String login) {
        UserCredentials userCredentials = userCredentialsRepo.findByLogin(login).orElseThrow();
        return userCredentials.getBottomReferrals().size();
    }

    @Named("getEarnedAmount")
    public double getEarnedAmount(String login) {
        UserCredentials userCredentials = userCredentialsRepo.findByLogin(login).orElseThrow();
        return userCredentials.getIncomes().stream().mapToDouble(Income::getTransactionAmount).sum();
    }
}