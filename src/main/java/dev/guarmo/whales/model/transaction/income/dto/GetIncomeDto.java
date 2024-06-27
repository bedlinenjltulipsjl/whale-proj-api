package dev.guarmo.whales.model.transaction.income.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetIncomeDto {
    private Long id;
    private Double transactionAmount;
    private LocalDateTime createdAt;
    private String incomeCausedByUserTgName;
    private String incomeSendToTgName;
}
