package dev.guarmo.whales.model.transaction.deposit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDepositDto {
    @JsonProperty(value = "id")
    private Long transactionId;
    @JsonProperty(value = "amount")
    private Double transactionAmount;
    private String address;
    @JsonProperty(value = "dest_tag")
    private String destTag;
    private String type;
    private String label;
    private String currency;
    private String status;
    @JsonProperty(value = "blockchain_confirmations")
    private Integer blockchainConfirmations;
    private String fee;
    @JsonProperty(value = "blockchain_hash")
    private String blockchainHash;
    private String error;
}
