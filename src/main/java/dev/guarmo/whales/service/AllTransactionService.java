package dev.guarmo.whales.service;

import dev.guarmo.whales.helper.UserHelper;
import dev.guarmo.whales.model.transaction.GetTransaction;
import dev.guarmo.whales.model.transaction.deposit.mapper.DepositMapper;
import dev.guarmo.whales.model.transaction.income.mapper.IncomeMapper;
import dev.guarmo.whales.model.transaction.purchase.mapper.PurchaseMapper;
import dev.guarmo.whales.model.transaction.withdraw.mapper.WithdrawMapper;
import dev.guarmo.whales.model.user.UserCredentials;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AllTransactionService {
    private final DepositMapper depositMapper;
    private final WithdrawMapper withdrawMapper;
    private final PurchaseMapper purchaseMapper;
    private final IncomeMapper incomeMapper;
    private final UserHelper userHelper;

    public List<GetTransaction> getAllTypesOfTransactions(String name) {
        UserCredentials model = userHelper.findByLoginModel(name);
        return getAllTypesOfTransactionsByUser(model);
    }

    public List<GetTransaction> getAllTypesOfTransactionsByUser(UserCredentials model) {
        List<GetTransaction> deposits =
                model.getDeposits()
                        .stream()
                        .map(depositMapper::toGetTransaction)
                        .toList();
        List<GetTransaction> withdraws =
                model.getWithdraws()
                        .stream()
                        .map(withdrawMapper::toGetTransaction)
                        .toList();
        List<GetTransaction> purchases =
                model.getPurchases()
                        .stream()
                        .map(purchaseMapper::toGetTransaction)
                        .toList();
        List<GetTransaction> incomes =
                model.getIncomes()
                        .stream()
                        .map(incomeMapper::toGetTransaction)
                        .toList();

        List<GetTransaction> allTransactions = new ArrayList<>();
        allTransactions.addAll(withdraws);
        allTransactions.addAll(deposits);
        allTransactions.addAll(incomes);
        allTransactions.addAll(purchases);
        allTransactions.sort((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()));
        return allTransactions;
    }
}
