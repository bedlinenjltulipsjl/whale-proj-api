package dev.guarmo.whales.service;

import dev.guarmo.whales.helper.InvestModelHelper;
import dev.guarmo.whales.helper.UserHelper;
import dev.guarmo.whales.model.investmodel.InvestModel;
import dev.guarmo.whales.model.investmodel.InvestModelLevel;
import dev.guarmo.whales.model.investmodel.InvestModelStatus;
import dev.guarmo.whales.model.transaction.income.Income;
import dev.guarmo.whales.model.transaction.income.IncomeType;
import dev.guarmo.whales.model.transaction.income.mapper.IncomeMapper;
import dev.guarmo.whales.model.user.UserCredentials;
import dev.guarmo.whales.repository.IncomeRepo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeService {
    private final IncomeMapper incomeMapper;
    private final IncomeRepo incomeRepo;
    private final UserHelper userHelper;
    private final InvestModelHelper investModelHelper;

    @Value("${referral.lvl1.part}")
    private Double referralLvl1PartInPercents;
    @Value("${referral.lvl2.part}")
    private Double referralLvl2PartInPercents;
    @Value("${referral.lvl3.part}")
    private Double referralLvl3PartInPercents;
    @Value("${app.admin.login}")
    private String adminLogin;
    private UserCredentials adminThatReceiveBonusWhenNobodyInLine;

    @PostConstruct
    private void init() {
        adminThatReceiveBonusWhenNobodyInLine = userHelper.findByLoginModel(adminLogin);
    }

    public List<UserCredentials> linkBonusToUpperReferrals(
            Double amount, InvestModelLevel investModelLevel,
            UserCredentials userThatSendsBonuses) {

        UserCredentials userThanGetsBonusLvl1 = getUpperReferralOrAdmin(
                userThatSendsBonuses,
                userThatSendsBonuses.getUpperReferral(),
                investModelLevel,
                referralLvl1PartInPercents,
                amount
        );

        UserCredentials userThanGetsBonusLvl2 = getUpperReferralOrAdmin(
                userThatSendsBonuses,
                userThanGetsBonusLvl1.getUpperReferral(),
                investModelLevel,
                referralLvl2PartInPercents,
                amount
        );

        UserCredentials userThanGetsBonusLvl3 = getUpperReferralOrAdmin(
                userThatSendsBonuses,
                userThanGetsBonusLvl2.getUpperReferral(),
                investModelLevel,
                referralLvl3PartInPercents,
                amount
        );

        return List.of(userThanGetsBonusLvl1, userThanGetsBonusLvl2, userThanGetsBonusLvl3);
    }

    private UserCredentials getUpperReferralOrAdmin(UserCredentials userThatSendsBonuses,
                                                    UserCredentials upperReferral,
                                                    InvestModelLevel investModelLevel,
                                                    Double referralPartInPercents,
                                                    Double amount) {
        if (upperReferral == null) {
            return adminThatReceiveBonusWhenNobodyInLine;
        }
        InvestModel upperReferralTable = investModelHelper.findUserInvestModelByLevel(upperReferral, investModelLevel);
        if (upperReferralTable.getInvestModelStatus().equals(InvestModelStatus.BOUGHT)
                || upperReferralTable.getInvestModelStatus().equals(InvestModelStatus.JUSTBOUGHT)
                || upperReferralTable.getInvestModelStatus().equals(InvestModelStatus.FROZEN)) {
            return createLinkAndSaveRefBonusToUser(amount, referralPartInPercents,
                    investModelLevel, userThatSendsBonuses, upperReferral, IncomeType.REFERRAL);
        } else {
            return createLinkAndSaveRefBonusToUser(amount, referralPartInPercents,
                    investModelLevel, userThatSendsBonuses, upperReferral, IncomeType.LOST);
        }

    }


    public UserCredentials createAndLinkAndSaveMainBonusToUser(Double amount,
                                                               double referralPartInPercents,
                                                               InvestModelLevel investModelLevel,
                                                               UserCredentials userThatSendsBonuses,
                                                               UserCredentials bonusTo) {
        UserCredentials userWithLinkedBonus = createLinkAndSaveBonusToUser(
                amount, referralPartInPercents,
                investModelLevel, userThatSendsBonuses,
                bonusTo, IncomeType.MAIN);

        InvestModel tableThatGivesMoney = investModelHelper.findUserInvestModelByLevel(userWithLinkedBonus, investModelLevel);
        investModelHelper.tableReceivedMainBonus(tableThatGivesMoney, userWithLinkedBonus);

        return userWithLinkedBonus;
    }

    public UserCredentials createLinkAndSaveRefBonusToUser(Double amount,
                                                            double referralPartInPercents,
                                                            InvestModelLevel investModelLevel,
                                                            UserCredentials userThatSendsBonuses,
                                                            UserCredentials bonusTo,
                                                           IncomeType incomeType) {
        return createLinkAndSaveBonusToUser(
                amount, referralPartInPercents,
                investModelLevel, userThatSendsBonuses,
                bonusTo, incomeType);
    }

    private UserCredentials createLinkAndSaveBonusToUser(
            Double amount,
            double referralPartInPercents,
            InvestModelLevel investModelLevel,
            UserCredentials userThatSendsBonuses,
            UserCredentials bonusTo,
            IncomeType incomeType) {

        Income income = incomeMapper.createBonusWithSenderAndAmount(
                amount * referralPartInPercents,
                investModelLevel,
                userThatSendsBonuses,
                incomeType);

        Income savedIncome = incomeRepo.save(income);
        bonusTo.getIncomes().add(savedIncome);

        if (incomeType.equals(IncomeType.REFERRAL)
        || incomeType.equals(IncomeType.MAIN)) {
            bonusTo.setBalanceAmount(
                    bonusTo.getBalanceAmount()
                            + savedIncome.getTransactionAmount());
        }
//        bonusTo.getIncomes().add(income);
//        bonusTo.setBalanceAmount(
//                bonusTo.getBalanceAmount()
//                        + income.getTransactionAmount());
        return bonusTo;
    }
}
