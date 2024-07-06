package dev.guarmo.whales.service;

import dev.guarmo.whales.helper.InvestModelHelper;
import dev.guarmo.whales.helper.UserHelper;
import dev.guarmo.whales.model.investmodel.InvestModelLevel;
import dev.guarmo.whales.model.investmodel.InvestModelStatus;
import dev.guarmo.whales.model.user.UserCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReferralBonusService {
    private final UserHelper userHelper;
    private final InvestModelHelper investModelHelper;
    @Value("${referral.bonus.part}")
    private Double bonusInPercentsFromPurchase;
    @Value("${app.admin.login}")
    private String adminLogin;
    @Value("${max.bonuses.before.freeze}")
    private Integer maxBonusesBeforeFreeze;
    private final IncomeService incomeService;

    public UserCredentials linkMainBonusToRandomReferral(
            Double purchaseAmount,
            InvestModelLevel investModelType,
            UserCredentials userCredentials) {

        UserCredentials randomUserThatGetsMainBonus = specifyUserThatGetsMainBonus(investModelType);

        UserCredentials randomUserWithLinkedMainBonus = incomeService.createAndLinkAndSaveMainBonusToUser(
                purchaseAmount,
                bonusInPercentsFromPurchase,
                investModelType,
                userCredentials,
                randomUserThatGetsMainBonus
        );
        log.info("User with linked bonus: {}", randomUserWithLinkedMainBonus);
        return randomUserWithLinkedMainBonus;
    }

//    private UserCredentials getTopTenUsersOrLess(
//            UserCredentials userCredentials,
//            InvestModelLevel desiredLevel) {
//
//        int i = 0;
//        List<UserCredentials> referralsAbove = new ArrayList<>();
//        userCredentials = userCredentials.getUpperReferral();
//
//        while (i < maxRefDepthSearch && userCredentials != null) {
//            referralsAbove.add(userCredentials);
//            userCredentials = userCredentials.getUpperReferral();
//            i++;
//        }
//
//        List<UserCredentials> referralsAboveFiltered = filterUsersByModelLevelAndStatus(referralsAbove, desiredLevel, InvestModelStatus.BOUGHT);
//
//        if (referralsAboveFiltered.isEmpty()) {
//            return userHelper.findByLoginModel(adminLogin);
//        } else {
//            Random random = new Random();
//            int size = referralsAboveFiltered.size();
//            int randomIndex = random.nextInt(size);
//            return referralsAboveFiltered.get(randomIndex);
//        }
//    }

    private UserCredentials specifyUserThatGetsMainBonus(
            InvestModelLevel desiredLevel) {

        List<UserCredentials> randomUsersToGetBonus = userHelper.findAllUsers().stream()
                .filter(u -> u.getInvestModels().stream()
                        .anyMatch(i -> !i.getIsReceivedBonus()
                                && i.getInvestModelStatus().equals(InvestModelStatus.BOUGHT)
                                && i.getDetails().getInvestModelLevel().equals(desiredLevel)
                                &&  investModelHelper.getCyclesOfThisTableForUser(u, desiredLevel) < maxBonusesBeforeFreeze
                        )
                ).toList();

        log.info("List {} {}", desiredLevel, randomUsersToGetBonus);
        if (randomUsersToGetBonus.isEmpty()) {
            return userHelper.findByLoginModel(adminLogin);
        } else {
            Random random = new Random();
            int size = randomUsersToGetBonus.size();
            int randomIndex = random.nextInt(size);
            return randomUsersToGetBonus.get(randomIndex);
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
