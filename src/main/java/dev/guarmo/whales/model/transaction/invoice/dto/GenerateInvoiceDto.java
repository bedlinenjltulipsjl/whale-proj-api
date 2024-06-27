package dev.guarmo.whales.model.transaction.invoice.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GenerateInvoiceDto {
    private String login;
    private String email;
    private String currencyCode;
    private Double transactionAmount;
}
