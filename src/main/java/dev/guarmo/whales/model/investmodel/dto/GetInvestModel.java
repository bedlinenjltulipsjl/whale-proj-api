package dev.guarmo.whales.model.investmodel.dto;

import dev.guarmo.whales.model.investmodel.InvestModelLevel;
import dev.guarmo.whales.model.investmodel.InvestModelStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class GetInvestModel {
    private String id;
    private String naming;
    private Double priceAmount;
    private Integer cyclesCount;
    private Integer cyclesBeforeFreezeNumber;
    private Integer cyclesBeforeFinishedNumber;
    private InvestModelStatus investModelStatus;
    private InvestModelLevel investModelLevel;
    private LocalDateTime unlockDate;
    private LocalDateTime createdAt;
}
