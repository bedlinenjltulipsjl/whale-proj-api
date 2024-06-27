package dev.guarmo.whales.model.transaction.withdraw;

import dev.guarmo.whales.model.transaction.PayTransaction;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class MoneyWithdraw extends PayTransaction {
    private String currency;
    private String cryptoAddress;
    private String description;
    private String email;
    private WithdrawStatus withdrawStatus;
}
