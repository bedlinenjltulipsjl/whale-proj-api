package dev.guarmo.whales.model.transaction.invoice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetInvoiceDto {
    private String token;
    private String url;
    private String expireAt;
    private String currencyCode;
    private Double transactionAmount;
    private String address;
    private Long label;
    private String destTag;
    private String successUrl;
    private String error;
}
