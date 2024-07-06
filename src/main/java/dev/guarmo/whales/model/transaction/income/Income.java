package dev.guarmo.whales.model.transaction.income;

import dev.guarmo.whales.model.investmodel.InvestModelLevel;
import dev.guarmo.whales.model.transaction.PayTransaction;
import dev.guarmo.whales.model.user.UserCredentials;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Income extends PayTransaction {
    @ManyToOne
    private UserCredentials incomeCausedByUser;
    private InvestModelLevel purchasedModel;
    private IncomeType incomeType;
    private Integer receivedLastBonusAt;
}
