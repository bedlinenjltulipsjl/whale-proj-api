package dev.guarmo.whales.service;

import dev.guarmo.whales.model.investmodel.InvestModelLevel;
import dev.guarmo.whales.model.transaction.income.Income;
import dev.guarmo.whales.model.transaction.income.dto.GetIncomeDto;
import dev.guarmo.whales.model.transaction.income.mapper.IncomeMapper;
import dev.guarmo.whales.model.user.UserCredentials;
import dev.guarmo.whales.repository.IncomeRepo;
import dev.guarmo.whales.repository.UserCredentialsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeService {
    private final UserCredentialsRepo userCredentialsRepo;
    private final IncomeMapper incomeMapper;
    private final IncomeRepo incomeRepo;

    @Value("${referral.lvl1.part}")
    private Double referralLvl1PartInPercents;
    @Value("${referral.lvl2.part}")
    private Double referralLvl2PartInPercents;
    @Value("${referral.lvl3.part}")
    private Double referralLvl3PartInPercents;

    public List<GetIncomeDto> addBonusUpperReferrals(Double amount, InvestModelLevel investModelLevel, UserCredentials userThatSendsBonuses) {

        UserCredentials bonusToLvl1 = userThatSendsBonuses.getUpperReferral();
        UserCredentials bonusToLvl2 = bonusToLvl1.getUpperReferral();
        UserCredentials bonusToLvl3 = bonusToLvl2.getUpperReferral();

        Income bonusLvl1 = createAndAddBonusToUser(amount, referralLvl1PartInPercents, investModelLevel, userThatSendsBonuses, bonusToLvl1);
        Income bonusLvl2 = createAndAddBonusToUser(amount, referralLvl2PartInPercents, investModelLevel, userThatSendsBonuses, bonusToLvl2);
        Income bonusLvl3 = createAndAddBonusToUser(amount, referralLvl3PartInPercents, investModelLevel, userThatSendsBonuses, bonusToLvl3);

        GetIncomeDto getIncomeDto1 = mapSomeFieldsIncomeDto(bonusLvl1, userThatSendsBonuses, bonusToLvl1);
        GetIncomeDto getIncomeDto2 = mapSomeFieldsIncomeDto(bonusLvl2, userThatSendsBonuses, bonusToLvl2);
        GetIncomeDto getIncomeDto3 = mapSomeFieldsIncomeDto(bonusLvl3, userThatSendsBonuses, bonusToLvl3);

        return List.of(getIncomeDto1, getIncomeDto2, getIncomeDto3);
    }

    public Income createAndAddBonusToUser(
            Double amount,
            double referralPartInPercents,
            InvestModelLevel investModelLevel,
            UserCredentials userThatSendsBonuses,
            UserCredentials bonusTo) {

        Income income = incomeMapper.createBonusWithSenderAndAmount(amount * referralPartInPercents, investModelLevel, userThatSendsBonuses);
        Income savedIncome = incomeRepo.save(income);

        bonusTo.getIncomes().add(savedIncome);
        bonusTo.setBalanceAmount(
                bonusTo.getBalanceAmount()
                + savedIncome.getTransactionAmount());

        userCredentialsRepo.save(bonusTo);
        return savedIncome;
    }

    private GetIncomeDto mapSomeFieldsIncomeDto(Income bonus, UserCredentials userThatSendsBonuses, UserCredentials bonusTo) {
        GetIncomeDto getIncomeDto = incomeMapper.toGetDto(bonus);
        getIncomeDto.setIncomeCausedByUserTgName(userThatSendsBonuses.getName());
        getIncomeDto.setIncomeSendToTgName(bonusTo.getName());
        return getIncomeDto;
    }

    public List<GetIncomeDto> findAllIncomesByLogin(String tgid) {
        return userCredentialsRepo
                .findByLogin(tgid)
                .orElseThrow()
                .getIncomes()
                .stream()
                .map(incomeMapper::toGetDto)
                .toList();
    }

    public List<Income> getIncomeModelsByLogin(String tgid) {
        return userCredentialsRepo
                .findByLogin(tgid)
                .orElseThrow()
                .getIncomes();
    }
}
