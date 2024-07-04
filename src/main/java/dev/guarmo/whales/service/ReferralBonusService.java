package dev.guarmo.whales.service;

import dev.guarmo.whales.helper.InvestModelHelper;
import dev.guarmo.whales.helper.UserHelper;
import dev.guarmo.whales.model.investmodel.InvestModel;
import dev.guarmo.whales.model.investmodel.InvestModelLevel;
import dev.guarmo.whales.model.investmodel.InvestModelStatus;
import dev.guarmo.whales.model.transaction.income.dto.GetIncomeDto;
import dev.guarmo.whales.model.transaction.income.mapper.IncomeMapper;
import dev.guarmo.whales.model.user.UserCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReferralBonusService {
    private final UserHelper userHelper;
    @Value("${referral.bonus.part}")
    private Double bonusInPercentsFromPurchase;
    @Value("${app.admin.login}")
    private String adminLogin;
    @Value("${search.referral.depth}")
    private Integer maxRefDepthSearch;
    private final IncomeService incomeService;

    public UserCredentials linkBonusToOneOfTenReferrals(
            Double purchaseAmount,
            InvestModelLevel investModelType,
            UserCredentials userCredentials) {

        UserCredentials randomFromTopTenReferrals = getRandomFromTopTenReferrals(userCredentials, investModelType);
        UserCredentials randomUserWithLinkedBonus = incomeService.createAndLinkBonusToUser(
                purchaseAmount,
                bonusInPercentsFromPurchase,
                investModelType,
                userCredentials,
                randomFromTopTenReferrals
        );
//        InvestModel investModel = investModelService.addCyclesToTableAfterAddingIncome(
//        investModelType, randomFromTopTenReferrals);
        log.info("Link bonus to user: {}", randomUserWithLinkedBonus);
        return randomUserWithLinkedBonus;
    }

    private UserCredentials getRandomFromTopTenReferrals(
            UserCredentials userCredentials,
            InvestModelLevel desiredLevel) {

        int i = 0;
        List<UserCredentials> referralsAbove = new ArrayList<>();
        userCredentials = userCredentials.getUpperReferral();

        while (i < maxRefDepthSearch && userCredentials != null) {
            referralsAbove.add(userCredentials);
            userCredentials = userCredentials.getUpperReferral();
            i++;
        }

        List<UserCredentials> referralsAboveFiltered = filterUsersByModelLevelAndStatus(referralsAbove, desiredLevel, InvestModelStatus.BOUGHT);

        if (referralsAboveFiltered.isEmpty()) {
            return userHelper.findByLoginModel(adminLogin);
        } else {
            Random random = new Random();
            int size = referralsAboveFiltered.size();
            int randomIndex = random.nextInt(size);
            return referralsAboveFiltered.get(randomIndex);
        }
    }

    public List<UserCredentials> filterUsersByModelLevelAndStatus(
            List<UserCredentials> users,
            InvestModelLevel desiredLevel,
            InvestModelStatus desiredStatus) {

        return users.stream()
                .filter(user -> user.getInvestModels().stream()
                        .anyMatch(model -> model
                                .getDetails()
                                .getInvestModelLevel() == desiredLevel
                                && model.getInvestModelStatus().equals(desiredStatus)))
                .collect(Collectors.toList());
    }
}
