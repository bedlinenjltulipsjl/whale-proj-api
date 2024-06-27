package dev.guarmo.whales.model.transaction.withdraw.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostWithdrawDto {
    private Double transactionAmount;
    private String currency;
    private String cryptoAddress;
    private String description;
    private String email;
}
