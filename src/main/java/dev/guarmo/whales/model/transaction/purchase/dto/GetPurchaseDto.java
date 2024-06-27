package dev.guarmo.whales.model.transaction.purchase.dto;

import java.time.LocalDateTime;

public class GetPurchaseDto {
    private Long id;
    private Double transactionAmount;
    private String description;
    private LocalDateTime createdAt;
}
