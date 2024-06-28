package dev.guarmo.whales.service;

import dev.guarmo.whales.model.investmodel.InvestModel;
import dev.guarmo.whales.model.investmodel.InvestModelLevel;
import dev.guarmo.whales.model.investmodel.InvestModelStatus;
import dev.guarmo.whales.repository.InvestModelRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class InvestModelService {
    private final InvestModelRepo investModelRepo;

    public List<InvestModel> generateDefaultInvestModels() {
        List<InvestModel> investModels = getInvestModelsList();
        return investModelRepo.saveAll(investModels);
    }

    public List<InvestModel> getInvestModelsList() {
        InvestModel[] investModels = new InvestModel[16];

        for (int i = 0; i < 16; i++) {
            investModels[i] = generateInvestModel(i);
        }

        investModels[9].setInvestModelStatus(InvestModelStatus.FROZEN);
        investModels[10].setInvestModelStatus(InvestModelStatus.FINISHED);
        investModels[11].setInvestModelStatus(InvestModelStatus.TIMELOCKED);
        investModels[12].setInvestModelStatus(InvestModelStatus.AVAILABLE);
        investModels[13].setInvestModelStatus(InvestModelStatus.LOCKED);
        investModels[14].setInvestModelStatus(InvestModelStatus.SPECIALS);

        return Arrays.stream(investModels).toList();
    }

    private InvestModel generateInvestModel(int i) {
        InvestModel investModel = new InvestModel();
        investModel.setNaming("InvestModel " + i);
        investModel.setPriceAmount(100.0 * (i + 1)); // Example price calculation
        investModel.setCyclesCount(i + 1); // Example cycles count
        investModel.setCyclesBeforeFreezeCount(i + 2); // Example cycles before freeze count
        investModel.setInvestModelLevel(InvestModelLevel.values()[i]); // Assigning levels sequentially

        // Assigning statuses in increasing order from 1 to 16
        investModel.setInvestModelStatus(InvestModelStatus.BOUGHT);
        return investModel;
    }
}
