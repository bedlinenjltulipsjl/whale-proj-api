package dev.guarmo.whales.controller;

import dev.guarmo.whales.model.transaction.invoice.dto.GenerateInvoiceDto;
import dev.guarmo.whales.model.transaction.invoice.dto.GetInvoiceDto;
import dev.guarmo.whales.model.transaction.deposit.dto.GetDepositDto;
import dev.guarmo.whales.model.user.dto.GetUserCredentialsDto;
import dev.guarmo.whales.service.InvoiceGeneratorService;
import dev.guarmo.whales.service.DepositService;
import dev.guarmo.whales.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/deposits")
public class DepositController {
    private final InvoiceGeneratorService invoiceGeneratorService;
    private final UserService userService;
    private final DepositService depositService;

    @GetMapping
    public List<GetDepositDto> getAllTransactionsFromUser(Authentication authentication) {
        return depositService.findAllTransactionsByLogin(authentication.getName());
    }

    @PostMapping("/getlink")
    public GetInvoiceDto generateInvoiceLink(@RequestBody GenerateInvoiceDto generateInvoiceDto) {
        GetUserCredentialsDto userCredentialsDto = userService.getByCredentialsByLogin(generateInvoiceDto.getLogin());
        return invoiceGeneratorService.generateInvoicePageObject(
                userCredentialsDto.getLogin(),
                generateInvoiceDto.getCurrencyCode(),
                generateInvoiceDto.getTransactionAmount(),
                generateInvoiceDto.getEmail()
        );
    }
}
