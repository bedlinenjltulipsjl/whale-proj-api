package dev.guarmo.whales.service;

import dev.guarmo.whales.model.transaction.GetTransaction;
import dev.guarmo.whales.model.transaction.deposit.mapper.DepositMapper;
import dev.guarmo.whales.model.transaction.income.mapper.IncomeMapper;
import dev.guarmo.whales.model.transaction.purchase.dto.GetPurchaseDto;
import dev.guarmo.whales.model.transaction.purchase.mapper.PurchaseMapper;
import dev.guarmo.whales.model.transaction.withdraw.mapper.WithdrawMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AllTransactionService {
    private final WithdrawService withdrawService;
    private final PurchaseService purchaseService;
    private final IncomeService incomeService;
    private final DepositService depositService;

    private final DepositMapper depositMapper;
    private final WithdrawMapper withdrawMapper;
    private final PurchaseMapper purchaseMapper;
    private final IncomeMapper incomeMapper;

    public List<GetTransaction> getAllTypesOfTransactions(String name) {
        List<GetTransaction> deposits =
                depositService.findAllDepositModelsByLogin(name)
                        .stream()
                        .map(depositMapper::toGetTransaction)
                        .toList();
        List<GetTransaction> withdraws =
                withdrawService.getWithdrawModelsByLogin(name)
                        .stream()
                        .map(withdrawMapper::toGetTransaction)
                        .toList();
        List<GetTransaction> purchases =
                purchaseService.getPurchaseModelsByUser(name)
                        .stream()
                        .map(purchaseMapper::toGetTransaction)
                        .toList();
        List<GetTransaction> incomes =
                incomeService.getIncomeModelsByLogin(name)
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
