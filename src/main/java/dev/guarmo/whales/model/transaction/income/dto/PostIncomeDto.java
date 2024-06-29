package dev.guarmo.whales.model.transaction.income.dto;

 import dev.guarmo.whales.model.investmodel.InvestModelLevel;
 import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostIncomeDto {
    private Double transactionAmount;
    private InvestModelLevel purchasedModel;
    private String incomeCausedByUserTgName;
}
