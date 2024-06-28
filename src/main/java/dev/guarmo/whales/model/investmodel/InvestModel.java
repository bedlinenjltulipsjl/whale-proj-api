package dev.guarmo.whales.model.investmodel;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class InvestModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String naming;
    private Double priceAmount;
    private Integer cyclesCount;
    private Integer cyclesBeforeFreezeNumber;
    private Integer cyclesBeforeFinishedNumber;
    private InvestModelStatus investModelStatus;
    private InvestModelLevel investModelLevel;
    @Column(updatable = false)
    private LocalDateTime unlockDate;
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
