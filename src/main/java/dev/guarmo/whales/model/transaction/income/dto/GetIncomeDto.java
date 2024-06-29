package dev.guarmo.whales.model.transaction.income.dto;

import dev.guarmo.whales.model.investmodel.InvestModelLevel;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetIncomeDto {
    private Long id;
    private Double transactionAmount;
    private LocalDateTime createdAt;
    private InvestModelLevel purchasedModel;
    private String incomeCausedByUserTgName;
    private String incomeSendToTgName;
}
