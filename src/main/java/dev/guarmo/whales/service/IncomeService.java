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


    public List<UserCredentials> linkBonusToUpperReferrals(
            Double amount, InvestModelLevel investModelLevel,
            UserCredentials userThatSendsBonuses) {

        UserCredentials adminThatReceiveBonusWhenNobodyInLine =
                userHelper.findByLoginModel(adminLogin);

        UserCredentials userThanGetsBonusLvl1 = getUpperReferralOrAdmin(userThatSendsBonuses.getUpperReferral(),
                adminThatReceiveBonusWhenNobodyInLine, investModelLevel);
        UserCredentials userThanGetsBonusLvl1Upd = createLinkAndSaveRefBonusToUser(amount, referralLvl1PartInPercents,
                investModelLevel, userThatSendsBonuses, userThanGetsBonusLvl1);

        UserCredentials userThanGetsBonusLvl2 = getUpperReferralOrAdmin(userThanGetsBonusLvl1.getUpperReferral(),
                adminThatReceiveBonusWhenNobodyInLine, investModelLevel);
        UserCredentials userThanGetsBonusLvl2Upd = createLinkAndSaveRefBonusToUser(amount, referralLvl2PartInPercents,
                investModelLevel, userThatSendsBonuses, userThanGetsBonusLvl2);

        UserCredentials userThanGetsBonusLvl3 = getUpperReferralOrAdmin(userThanGetsBonusLvl2.getUpperReferral(),
                adminThatReceiveBonusWhenNobodyInLine, investModelLevel);
        UserCredentials userThanGetsBonusLvl3Upd = createLinkAndSaveRefBonusToUser(amount, referralLvl3PartInPercents,
                investModelLevel, userThatSendsBonuses, userThanGetsBonusLvl3);

        return List.of(userThanGetsBonusLvl1Upd, userThanGetsBonusLvl2Upd, userThanGetsBonusLvl3Upd);
    }

    private UserCredentials getUpperReferralOrAdmin(UserCredentials upperReferral,
                                                    UserCredentials adminThatReceiveBonusWhenNobodyInLine, InvestModelLevel investModelLevel) {
        if (upperReferral == null) {
            return adminThatReceiveBonusWhenNobodyInLine;
        }
        InvestModel upperReferralTable = investModelHelper.findUserInvestModelByLevel(upperReferral, investModelLevel);
        if (upperReferralTable.getInvestModelStatus().equals(InvestModelStatus.BOUGHT)
                || upperReferralTable.getInvestModelStatus().equals(InvestModelStatus.JUSTBOUGHT)
                || upperReferralTable.getInvestModelStatus().equals(InvestModelStatus.FROZEN)) {
            return upperReferral;
        } else {
            return adminThatReceiveBonusWhenNobodyInLine;
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
                                                            UserCredentials bonusTo) {
        return createLinkAndSaveBonusToUser(
                amount, referralPartInPercents,
                investModelLevel, userThatSendsBonuses,
                bonusTo, IncomeType.REFERRAL);
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

//        Income savedIncome = incomeRepo.save(income);

//        bonusTo.getIncomes().add(savedIncome);
//        bonusTo.setBalanceAmount(
//                bonusTo.getBalanceAmount()
//                        + savedIncome.getTransactionAmount());

        return bonusTo;
    }
}
