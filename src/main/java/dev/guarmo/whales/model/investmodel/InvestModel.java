package dev.guarmo.whales.model.investmodel;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class InvestModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String naming;
    private Double priceAmount;
    private Integer cyclesCount;
    private Integer cyclesBeforeFreezeCount;
    private InvestModelStatus investModelStatus;
    private InvestModelLevel investModelLevel;
    @Column(updatable = false)
    private LocalDateTime unlockDate;
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
