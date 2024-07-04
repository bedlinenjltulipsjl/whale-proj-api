package dev.guarmo.whales.model.investmodel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

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
    private Integer cyclesBeforeFreezeNumber;
    private Integer cyclesBeforeFinishedNumber;

    private InvestModelLevel investModelLevel;
    private InvestModelStatus defaultStatus;

}
