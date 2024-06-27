package dev.guarmo.whales.model.transaction.withdraw.dto;

import dev.guarmo.whales.model.transaction.withdraw.WithdrawStatus;
import lombok.Data;

@Data
public class GetWithdrawDto {
    private Long id;
    private Double transactionAmount;
    private String currency;
    private String cryptoAddress;
    private String description;
    private String email;
    private WithdrawStatus withdrawStatus;
}
