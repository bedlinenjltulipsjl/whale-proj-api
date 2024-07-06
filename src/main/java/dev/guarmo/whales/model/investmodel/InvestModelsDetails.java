package dev.guarmo.whales.model.investmodel;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvestModelsDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String naming;
    private Double priceAmount;
    private Integer cyclesCount;
    private Integer cyclesBeforeFinishedNumber;

    private InvestModelLevel investModelLevel;
    private InvestModelStatus defaultStatus;

    @Column(updatable = false)
    private LocalDateTime unlockDate;
}
