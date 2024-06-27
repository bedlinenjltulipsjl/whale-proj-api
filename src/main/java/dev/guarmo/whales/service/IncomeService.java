package dev.guarmo.whales.service;

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
    private final UserService userService;

    @Value("${referral.lvl1.part}")
    private Double referralLvl1PartInPercents;
    @Value("${referral.lvl2.part}")
    private Double referralLvl2PartInPercents;
    @Value("${referral.lvl3.part}")
    private Double referralLvl3PartInPercents;

    public List<GetIncomeDto> addBonusUpperReferrals(Double amount,
                                                     String bonusFromUserWithLogin) {
        UserCredentials userThatSendsBonuses = userCredentialsRepo
                .findByLogin(bonusFromUserWithLogin).orElseThrow();

        UserCredentials bonusToLvl1 = userThatSendsBonuses.getUpperReferral();
        UserCredentials bonusToLvl2 = bonusToLvl1.getUpperReferral();
        UserCredentials bonusToLvl3 = bonusToLvl2.getUpperReferral();

        Income income1 = incomeMapper.createBonusWithSenderAndAmount(
                amount*referralLvl2PartInPercents,
                userThatSendsBonuses);

        income1.setTransactionAmount(amount*referralLvl1PartInPercents);
        Income bonusLvl1 = incomeRepo.save(income1);
        bonusToLvl1.getIncomes().add(bonusLvl1);
        userCredentialsRepo.save(bonusToLvl1);

        Income income2 = incomeMapper.createBonusWithSenderAndAmount(
                amount*referralLvl2PartInPercents,
                userThatSendsBonuses);
        Income bonusLvl2 = incomeRepo.save(income2);
        bonusToLvl2.getIncomes().add(bonusLvl2);
        userCredentialsRepo.save(bonusToLvl2);

        Income income3 = incomeMapper.createBonusWithSenderAndAmount(
                amount*referralLvl3PartInPercents,
                userThatSendsBonuses);
        Income bonusLvl3 = incomeRepo.save(income3);
        bonusToLvl3.getIncomes().add(bonusLvl2);
        userCredentialsRepo.save(bonusToLvl3);

        // Set some additional information to be crystal clear
        GetIncomeDto getIncomeDto1 = incomeMapper.toGetDto(bonusLvl1);
        getIncomeDto1.setIncomeCausedByUserTgName(userThatSendsBonuses.getName());
        getIncomeDto1.setIncomeSendToTgName(bonusToLvl1.getName());

        // Set some additional information to be crystal clear
        GetIncomeDto getIncomeDto2 = incomeMapper.toGetDto(bonusLvl2);
        getIncomeDto2.setIncomeCausedByUserTgName(userThatSendsBonuses.getName());
        getIncomeDto2.setIncomeSendToTgName(bonusToLvl2.getName());

        // Set some additional information to be crystal clear
        GetIncomeDto getIncomeDto3 = incomeMapper.toGetDto(bonusLvl3);
        getIncomeDto3.setIncomeCausedByUserTgName(userThatSendsBonuses.getName());
        getIncomeDto3.setIncomeSendToTgName(bonusToLvl3.getName());
        return List.of(getIncomeDto1, getIncomeDto2, getIncomeDto3);
    }

    public List<GetIncomeDto> findAllIncomesByLogin(String tgid) {
        return userService.findByLoginModel(tgid).getIncomes()
                .stream()
                .map(incomeMapper::toGetDto)
                .toList();
    }
}
