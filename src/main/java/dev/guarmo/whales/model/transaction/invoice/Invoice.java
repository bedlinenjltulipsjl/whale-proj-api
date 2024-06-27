package dev.guarmo.whales.model.transaction.invoice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.guarmo.whales.model.transaction.PayTransaction;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Invoice extends PayTransaction {
    @JsonProperty("token")
    private String token;

    @JsonProperty("url")
    private String url;

    @JsonProperty("expire_at")
    private String expireAt;

    @JsonProperty("currency_code")
    private String currencyCode;

    @JsonProperty("address")
    private String address;

    @JsonProperty("dest_tag")
    private String destTag;

    @JsonIgnore
    private String label;

    @JsonIgnore
    private String email;

    @JsonProperty("success_url")
    private String successUrl;

    @JsonProperty("error")
    private String error;
}
