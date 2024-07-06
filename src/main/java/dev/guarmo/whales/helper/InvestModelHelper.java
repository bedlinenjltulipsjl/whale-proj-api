package dev.guarmo.whales.helper;

import dev.guarmo.whales.model.investmodel.InvestModel;
import dev.guarmo.whales.model.investmodel.InvestModelLevel;
import dev.guarmo.whales.model.investmodel.InvestModelStatus;
import dev.guarmo.whales.model.investmodel.InvestModelsDetails;
import dev.guarmo.whales.model.transaction.income.IncomeType;
import dev.guarmo.whales.model.user.UserCredentials;
import dev.guarmo.whales.repository.InvestModelRepo;
import dev.guarmo.whales.teleg.TelegramService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InvestModelHelper {
    private final InvestModelRepo investModelRepo;
    private final TelegramService telegramService;
    @Value("${max.bonuses.before.freeze}")
    private Integer maxBonusesBeforeFreeze;

    private static HashMap<InvestModelLevel, String> investModelLevelToNaming;

    @PostConstruct
    private void init() {
        investModelLevelToNaming = new HashMap<>();
        investModelLevelToNaming.put(InvestModelLevel.LEVEL_1, "Private");
        investModelLevelToNaming.put(InvestModelLevel.LEVEL_2, "Lvl 1");
        investModelLevelToNaming.put(InvestModelLevel.LEVEL_3, "Lvl 2");
        investModelLevelToNaming.put(InvestModelLevel.LEVEL_4, "Lvl 3");
        investModelLevelToNaming.put(InvestModelLevel.LEVEL_5, "Lvl 4");
        investModelLevelToNaming.put(InvestModelLevel.LEVEL_6, "Lvl 5");
        investModelLevelToNaming.put(InvestModelLevel.LEVEL_7, "Lvl 6");
        investModelLevelToNaming.put(InvestModelLevel.LEVEL_8, "Lvl 7");
        investModelLevelToNaming.put(InvestModelLevel.LEVEL_9, "Lvl 8");
        investModelLevelToNaming.put(InvestModelLevel.LEVEL_10, "Lvl 9");
        investModelLevelToNaming.put(InvestModelLevel.LEVEL_11, "Lvl 10");
        investModelLevelToNaming.put(InvestModelLevel.LEVEL_12, "Lvl 11");
        investModelLevelToNaming.put(InvestModelLevel.LEVEL_13, "Lvl 12");
        investModelLevelToNaming.put(InvestModelLevel.LEVEL_14, "Lvl 13");
        investModelLevelToNaming.put(InvestModelLevel.LEVEL_15, "Lvl 14");
        investModelLevelToNaming.put(InvestModelLevel.LEVEL_16, "Lvl 15");
    }

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
        String formated = String.format("BEFORE: User that receives income and his table %s\n\n%s", userWithTable, notReferralTable);
        log.info(formated);
        telegramService.sendTextNotification(formated);

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

            String formated2 = String.format("BEFORE: Cycle ended. Updating info in every table of %s after cycle is finished\n\n%s", investModelDetails.getInvestModelLevel(), filteredByLevel);
            log.info(formated2);

            // REFRESH RECEIVED BONUS TOGGLE
            filteredByLevel.forEach(i -> i.setIsReceivedBonus(false));
            // GET ONLY JUST BOUGHT AND SET THEM TO BOUGHT
            filteredByLevel.stream().filter(i -> i.getInvestModelStatus().equals(InvestModelStatus.JUSTBOUGHT)).forEach(i -> i.setInvestModelStatus(InvestModelStatus.BOUGHT));

            investModelDetails.setCyclesBeforeFinishedNumber(notReferralTable.getDetails().getCyclesBeforeFinishedNumber() * 2);
            investModelDetails.setCyclesCount(1);

            List<InvestModel> filteredByLevelAndSaved = investModelRepo.saveAll(filteredByLevel);
            String formated3 = String.format("AFTER: Cycle ended. Updating info in every table of %s after cycle is finished\n\n%s", investModelDetails.getInvestModelLevel(), filteredByLevelAndSaved);
            log.info(formated3);
            telegramService.sendTextNotification(formated3);
        }

        return investModelRepo.save(notReferralTable);
    }

    public static String getNameByInvestModelLevel(InvestModelLevel level) {
        return investModelLevelToNaming.get(level);
    }
}
