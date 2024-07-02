package dev.guarmo.whales.controller;

import dev.guarmo.whales.model.transaction.invoice.dto.GenerateInvoiceDto;
import dev.guarmo.whales.model.transaction.invoice.dto.GetInvoiceDto;
import dev.guarmo.whales.service.InvoiceGeneratorService;
import dev.guarmo.whales.service.DepositService;
import dev.guarmo.whales.teleg.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/deposits")
@CrossOrigin(allowedHeaders = "*")
public class DepositController {
    private final InvoiceGeneratorService invoiceGeneratorService;
    private final TelegramService telegramService;

    @PostMapping("/getlink")
    public GetInvoiceDto generateInvoiceLink(@RequestBody GenerateInvoiceDto generateInvoiceDto) {
        GetInvoiceDto getInvoiceDto = invoiceGeneratorService.generateInvoicePageObject(
                generateInvoiceDto.getLogin(),
                generateInvoiceDto.getCurrencyCode(),
                generateInvoiceDto.getTransactionAmount(),
                generateInvoiceDto.getEmail()
        );
        telegramService.sendNotificationAboutStartingInvoice(getInvoiceDto);
        return getInvoiceDto;
    }
}
