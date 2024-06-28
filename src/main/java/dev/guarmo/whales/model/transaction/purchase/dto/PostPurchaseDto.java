package dev.guarmo.whales.model.transaction.purchase.dto;

import dev.guarmo.whales.model.investmodel.InvestModelLevel;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostPurchaseDto {
    private Double transactionAmount;
    private InvestModelLevel purchasedModel;
}
