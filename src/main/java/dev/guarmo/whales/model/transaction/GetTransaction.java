package dev.guarmo.whales.model.transaction;

import dev.guarmo.whales.model.transaction.income.IncomeType;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
public class GetTransaction {
    private Double transactionAmount;
    private String description;
    private TransactionType transactionType;
    private IncomeType incomeType;
    private LocalDateTime createdAt;
}
