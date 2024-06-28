package dev.guarmo.whales.model.transaction.purchase.dto;

import dev.guarmo.whales.model.investmodel.InvestModelLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GetPurchaseDto {
    private Long id;
    private Double transactionAmount;
    private InvestModelLevel purchasedModel;
    private LocalDateTime createdAt;
}
