package dev.guarmo.whales.helper;

import dev.guarmo.whales.model.investmodel.InvestModel;
import dev.guarmo.whales.model.investmodel.InvestModelLevel;
import dev.guarmo.whales.model.investmodel.InvestModelStatus;
import dev.guarmo.whales.model.user.UserCredentials;
import dev.guarmo.whales.repository.UserCredentialsRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InvestModelHelper {

    public UserCredentials updateInvestEntities(UserCredentials user) {
        List<InvestModel> investModels = changeStatesAfterBuyingInvestEntity(user.getInvestModels());
        user.setInvestModels(investModels);
        return user;
    }

    private List<InvestModel> changeStatesAfterBuyingInvestEntity(List<InvestModel> investModels) {
        InvestModel investModel = investModels.stream().filter(m -> m.getInvestModelStatus() == InvestModelStatus.LOCKED).findFirst().orElseThrow();
        investModel.setInvestModelStatus(InvestModelStatus.MONEYLOCKED);
        return investModels;
    }

    public InvestModelLevel getNextLevelAsEnum(InvestModelLevel currentLevel) {
        int ordinal = currentLevel.ordinal();
        InvestModelLevel[] levels = InvestModelLevel.values();
        if (ordinal < levels.length - 1) {
            return levels[ordinal + 1];
        } else {
            log.error("This is the last table, next is NOT available");
            throw new RuntimeException("This is the last table, next is NOT available");
        }
    }

    public InvestModel addCyclesToInvestModel(InvestModel investModel, InvestModel nextInvestModel) {
        if (investModel.getCyclesCount() + 1 == investModel.getCyclesBeforeFreezeNumber()
                && nextInvestModel.getInvestModelStatus() != InvestModelStatus.BOUGHT) {
            investModel.setCyclesCount(investModel.getCyclesCount() + 1);
            investModel.setInvestModelStatus(InvestModelStatus.FROZEN);
        }

        if (investModel.getCyclesCount() + 1 == investModel.getCyclesBeforeFinishedNumber()) {
            investModel.setCyclesCount(investModel.getCyclesCount() + 1);
            investModel.setInvestModelStatus(InvestModelStatus.FINISHED);
        }
        return investModel;
    }
}
