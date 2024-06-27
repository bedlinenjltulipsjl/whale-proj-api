package dev.guarmo.whales.model.transaction.deposit;

import dev.guarmo.whales.model.transaction.PayTransaction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Deposit extends PayTransaction {
    private Long transactionId;
    private String address;
    private String destTag;
    private String label;
    private String currency;
    private String status;
    private Integer blockchainConfirmations;
    private String fee;
    private String blockchainHash;
}
