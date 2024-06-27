package dev.guarmo.whales.model.transaction.purchase;

import dev.guarmo.whales.model.transaction.PayTransaction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class Purchase extends PayTransaction {
    private Double purchaseAmount;
    private String description;
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
