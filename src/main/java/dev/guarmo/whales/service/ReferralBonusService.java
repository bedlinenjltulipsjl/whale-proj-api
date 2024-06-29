package dev.guarmo.whales.service;

import dev.guarmo.whales.model.transaction.income.dto.GetIncomeDto;
import org.springframework.beans.factory.annotation.Value;

public class ReferralBonusService {
    @Value("${referral.bonus.part}")
    private Double bonusInPercentsFromPurchase;

//    public GetIncomeDto setRandomlyIncomeTOOneOfTop10Referrals() {
//
//    }
}
