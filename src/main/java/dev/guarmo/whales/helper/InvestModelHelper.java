package dev.guarmo.whales.helper;

import dev.guarmo.whales.model.investmodel.InvestModel;
import dev.guarmo.whales.model.investmodel.InvestModelLevel;
import dev.guarmo.whales.model.investmodel.InvestModelStatus;
import dev.guarmo.whales.model.user.UserCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InvestModelHelper {

    public UserCredentials linkNewInvestEntities(UserCredentials user) {
        user.setInvestModels(
                changeStatesAfterBuyingInvestEntity(user.getInvestModels())
        );
        return user;
    }

    private List<InvestModel> changeStatesAfterBuyingInvestEntity(List<InvestModel> investModels) {
        InvestModel investModelToBought = investModels.stream().filter(m -> m.getInvestModelStatus() == InvestModelStatus.AVAILABLE).findFirst().orElseThrow();
        investModelToBought.setInvestModelStatus(InvestModelStatus.BOUGHT);

//        InvestModel investModelToAvailable = investModels.stream().filter(m -> m.getInvestModelStatus() == InvestModelStatus.MONEYLOCKED).findFirst().orElseThrow();
//        investModelToAvailable.setInvestModelStatus(InvestModelStatus.AVAILABLE);

        // Do not need to save array, working with links, everything
        // We change as objects changes inside array directly
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
        if (investModel.getCyclesCount() + 1 == investModel.getDetails().getCyclesBeforeFreezeNumber()
                && nextInvestModel.getInvestModelStatus() != InvestModelStatus.BOUGHT) {
            investModel.setCyclesCount(investModel.getCyclesCount() + 1);
            investModel.setInvestModelStatus(InvestModelStatus.FROZEN);
        }

        if (investModel.getCyclesCount() + 1 == investModel.getDetails().getCyclesBeforeFinishedNumber()) {
            investModel.setCyclesCount(investModel.getCyclesCount() + 1);
            investModel.setInvestModelStatus(InvestModelStatus.FINISHED);
        }
        return investModel;
    }

    public InvestModel findUserInvestModelByLevel(UserCredentials userCredentials, InvestModelLevel investModelLevel) {
        return userCredentials
                .getInvestModels()
                .stream()
                .filter(im -> im.getDetails().getInvestModelLevel() == investModelLevel)
                .findFirst().orElseThrow();
    }
}
