package dev.guarmo.whales.model.transaction.income.dto;

 import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostIncomeDto {
    private Double transactionAmount;
    private String incomeCausedByUserTgName;
}
