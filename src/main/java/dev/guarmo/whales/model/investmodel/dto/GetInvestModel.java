package dev.guarmo.whales.model.investmodel.dto;

import dev.guarmo.whales.model.investmodel.InvestModelLevel;
import dev.guarmo.whales.model.investmodel.InvestModelStatus;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
public class GetInvestModel {
    private Long id;
    private String naming;
    private Double priceAmount;
    private Integer cyclesCount;

    private Integer receivedBonusAtCycle;
    private Boolean isReceivedBonus;

    private Integer cyclesBeforeFinishedNumber;

    private Double partnerBonusAmount;
    private Double mainBonusAmount;

    private InvestModelStatus investModelStatus;
    private InvestModelLevel investModelLevel;
    private LocalDateTime unlockDate;
    private LocalDateTime createdAt;
}
