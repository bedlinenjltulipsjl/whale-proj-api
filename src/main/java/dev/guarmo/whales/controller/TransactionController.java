package dev.guarmo.whales.controller;

import dev.guarmo.whales.model.transaction.GetTransaction;
import dev.guarmo.whales.model.transaction.purchase.dto.GetPurchaseDto;
import dev.guarmo.whales.service.AllTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/transactions")
@CrossOrigin(allowedHeaders = "*")
public class TransactionController {
    private final AllTransactionService allTransactionService;

    @GetMapping
    public List<GetTransaction> getPurchaseDtoList(Authentication authentication) {
        return allTransactionService.getAllTypesOfTransactions(authentication.getName());
    }
}
