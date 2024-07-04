package dev.guarmo.whales.service;

import dev.guarmo.whales.helper.UserHelper;
import dev.guarmo.whales.model.investmodel.InvestModelLevel;
import dev.guarmo.whales.model.transaction.income.Income;
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

        UserCredentials userThanGetsBonusLvl1 = userThatSendsBonuses.getUpperReferral();
        if (userThanGetsBonusLvl1 == null) {
            userThanGetsBonusLvl1 = adminThatReceiveBonusWhenNobodyInLine;
        }
        UserCredentials userThanGetsBonusLvl1Upd = createAndLinkBonusToUser(amount, referralLvl1PartInPercents,
                investModelLevel, userThatSendsBonuses, userThanGetsBonusLvl1);

        UserCredentials userThanGetsBonusLvl2 = userThanGetsBonusLvl1.getUpperReferral();
        if (userThanGetsBonusLvl2 == null) {
            userThanGetsBonusLvl2 = adminThatReceiveBonusWhenNobodyInLine;
        }
        UserCredentials userThanGetsBonusLvl2Upd = createAndLinkBonusToUser(amount, referralLvl2PartInPercents,
                investModelLevel, userThatSendsBonuses, userThanGetsBonusLvl2);

        UserCredentials userThanGetsBonusLvl3 = userThanGetsBonusLvl2.getUpperReferral();
        if (userThanGetsBonusLvl3 == null) {
            userThanGetsBonusLvl3 = adminThatReceiveBonusWhenNobodyInLine;
        }
        UserCredentials userThanGetsBonusLvl3Upd = createAndLinkBonusToUser(amount, referralLvl3PartInPercents,
                investModelLevel, userThatSendsBonuses, userThanGetsBonusLvl3);

        return List.of(userThanGetsBonusLvl1Upd, userThanGetsBonusLvl2Upd, userThanGetsBonusLvl3Upd);
    }

    public UserCredentials createAndLinkBonusToUser(
            Double amount,
            double referralPartInPercents,
            InvestModelLevel investModelLevel,
            UserCredentials userThatSendsBonuses,
            UserCredentials bonusTo) {

        Income income = incomeMapper.createBonusWithSenderAndAmount(
                amount * referralPartInPercents, investModelLevel, userThatSendsBonuses);
        Income savedIncome = incomeRepo.save(income);

        bonusTo.getIncomes().add(savedIncome);
        bonusTo.setBalanceAmount(
                bonusTo.getBalanceAmount()
                + savedIncome.getTransactionAmount());

        return bonusTo;
    }

//    private GetIncomeDto mapSomeFieldsIncomeDto(Income bonus, UserCredentials userThatSendsBonuses, UserCredentials bonusTo) {
//        GetIncomeDto getIncomeDto = incomeMapper.toGetDto(bonus);
//        getIncomeDto.setIncomeCausedByUserTgName(userThatSendsBonuses.getName());
//        getIncomeDto.setIncomeSendToTgName(bonusTo.getName());
//        return getIncomeDto;
//    }

//    public List<GetIncomeDto> findAllIncomesByLogin(String tgid) {
//        return userCredentialsRepo
//                .findByLogin(tgid)
//                .orElseThrow()
//                .getIncomes()
//                .stream()
//                .map(incomeMapper::toGetDto)
//                .toList();
//    }

//    public List<Income> getIncomeModelsByLogin(String tgid) {
//        return userCredentialsRepo
//                .findByLogin(tgid)
//                .orElseThrow()
//                .getIncomes();
//    }
}
