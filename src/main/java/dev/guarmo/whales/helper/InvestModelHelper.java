package dev.guarmo.whales.helper;

import dev.guarmo.whales.model.investmodel.InvestModel;
import dev.guarmo.whales.model.investmodel.InvestModelLevel;
import dev.guarmo.whales.model.investmodel.InvestModelStatus;
import dev.guarmo.whales.model.investmodel.InvestModelsDetails;
import dev.guarmo.whales.model.transaction.income.IncomeType;
import dev.guarmo.whales.model.user.UserCredentials;
import dev.guarmo.whales.repository.InvestModelRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InvestModelHelper {
    private final InvestModelRepo investModelRepo;
    @Value("${max.bonuses.before.freeze}")
    private Integer maxBonusesBeforeFreeze;


    public Long getCyclesOfThisTableForUser(UserCredentials user, InvestModelLevel desiredLevel) {
        return user.getIncomes().stream()
                .filter(inc -> inc.getPurchasedModel().equals(desiredLevel)
                        && inc.getIncomeType().equals(IncomeType.MAIN))
                .count();
    }


    public InvestModel findUserInvestModelByLevel(UserCredentials userCredentials, InvestModelLevel investModelLevel) {
        return userCredentials
                .getInvestModels()
                .stream()
                .filter(im -> im.getDetails().getInvestModelLevel() == investModelLevel)
                .findFirst().orElseThrow();
    }

    public InvestModel tableReceivedMainBonus(InvestModel notReferralTable, UserCredentials userWithTable) {
        log.info("BEFORE: User that receives income and his table {} {}" , userWithTable, notReferralTable);
        notReferralTable.setReceivedBonusAtCycle(notReferralTable.getDetails().getCyclesCount());
        notReferralTable.setIsReceivedBonus(true);
        InvestModelsDetails investModelDetails = notReferralTable.getDetails();

        investModelDetails.setCyclesCount(investModelDetails.getCyclesCount() + 1);
        Long bonusesCountFromThisTableUserReceived = getCyclesOfThisTableForUser(
                userWithTable,
                investModelDetails.getInvestModelLevel());

        if (bonusesCountFromThisTableUserReceived >= maxBonusesBeforeFreeze) {
            notReferralTable.setInvestModelStatus(InvestModelStatus.FROZEN);
        }

        // Cycle passes, make every JUST BOUGHT of this table BOUGHT
        // Reset receive bonus flag in each TABLE of this level
        if (investModelDetails.getCyclesCount() > investModelDetails.getCyclesBeforeFinishedNumber()) {
            // FIND ALL TABLES OF THIS LEVEL
            List<InvestModel> filteredByLevel = investModelRepo.findAll().stream().filter(i -> i.getDetails().getInvestModelLevel().equals(investModelDetails.getInvestModelLevel())).toList();
            log.info("BEFORE: Updating info in every table {}", filteredByLevel);
            // REFRESH RECEIVED BONUS TOGGLE
            filteredByLevel.forEach(i -> i.setIsReceivedBonus(false));
            // GET ONLY JUST BOUGHT AND SET THEM TO BOUGHT
            filteredByLevel.stream().filter(i -> i.getInvestModelStatus().equals(InvestModelStatus.JUSTBOUGHT)).forEach(i -> i.setInvestModelStatus(InvestModelStatus.BOUGHT));

            investModelDetails.setCyclesBeforeFinishedNumber(notReferralTable.getDetails().getCyclesBeforeFinishedNumber() * 2);
            investModelDetails.setCyclesCount(1);
            log.info("AFTER: Updating info in every table {}", filteredByLevel);
            investModelRepo.saveAll(filteredByLevel);
        }

        return investModelRepo.save(notReferralTable);
    }
}
