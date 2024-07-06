package dev.guarmo.whales.service;

import dev.guarmo.whales.model.investmodel.InvestModelLevel;
import dev.guarmo.whales.model.investmodel.InvestModelStatus;
import dev.guarmo.whales.model.investmodel.InvestModelsDetails;
import dev.guarmo.whales.repository.InvestModelDetailsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvestModelDetailsService {
    private final InvestModelDetailsRepo investModelDetailsRepo;

    public List<InvestModelsDetails> saveBaseValues() {
        List<InvestModelsDetails> investModelsDetails = new ArrayList<>();
        investModelsDetails.add(
                buildInvestModelDetails("Private", 10.0, InvestModelLevel.LEVEL_1, 1, InvestModelStatus.AVAILABLE));
        investModelsDetails.add(
                buildInvestModelDetails("Lvl 1", 200.0, InvestModelLevel.LEVEL_2, 1, InvestModelStatus.LOCKED));
        investModelsDetails.add(
                buildInvestModelDetails("Lvl 2", 170.0, InvestModelLevel.LEVEL_3, 1, InvestModelStatus.LOCKED));
        investModelsDetails.add(
                buildInvestModelDetails("Lvl 3", 130.0, InvestModelLevel.LEVEL_4, 1, InvestModelStatus.LOCKED));
        investModelsDetails.add(
                buildInvestModelDetails("Lvl 4", 110.0, InvestModelLevel.LEVEL_5, 1, InvestModelStatus.LOCKED));
        investModelsDetails.add(
                buildInvestModelDetails("Lvl 5", 80.0, InvestModelLevel.LEVEL_6, 1, InvestModelStatus.LOCKED));
        investModelsDetails.add(
                buildInvestModelDetails("Lvl 6", 50.0, InvestModelLevel.LEVEL_7, 1, InvestModelStatus.LOCKED));
        investModelsDetails.add(
                buildInvestModelDetails("Lvl 7", 40.0, InvestModelLevel.LEVEL_8, 1, InvestModelStatus.LOCKED));
        investModelsDetails.add(
                buildInvestModelDetails("Lvl 8", 30.0, InvestModelLevel.LEVEL_9, 1, InvestModelStatus.LOCKED));
        investModelsDetails.add(
                buildInvestModelDetails("Lvl 9", 20.0, InvestModelLevel.LEVEL_10, 1, InvestModelStatus.LOCKED));
        investModelsDetails.add(
                buildInvestModelDetails("Lvl 10", 15.0, InvestModelLevel.LEVEL_11, 1, InvestModelStatus.LOCKED));
        investModelsDetails.add(
                buildInvestModelDetails("Lvl 11", 10.0, InvestModelLevel.LEVEL_12, 1, InvestModelStatus.LOCKED));

        investModelsDetails.add(
                buildInvestModelDetails("Lvl 12", 100.0, InvestModelLevel.LEVEL_13, 1, InvestModelStatus.SPECIALS));
        investModelsDetails.add(
                buildInvestModelDetails("Lvl 13", 150.0, InvestModelLevel.LEVEL_14, 1, InvestModelStatus.SPECIALS));
        investModelsDetails.add(
                buildInvestModelDetails("Lvl 14", 200.0, InvestModelLevel.LEVEL_15, 1, InvestModelStatus.SPECIALS));

        return investModelDetailsRepo.saveAll(investModelsDetails);
    }

    private static InvestModelsDetails buildInvestModelDetails(String naming, double priceAmount, InvestModelLevel level, int cyclesBeforeFinishedNumber, InvestModelStatus investModelStatus) {
        return InvestModelsDetails.builder()
                .naming(naming)
                .priceAmount(priceAmount)
                .investModelLevel(level)
                .cyclesCount(0)
                .cyclesBeforeFinishedNumber(cyclesBeforeFinishedNumber)
                .defaultStatus(investModelStatus)
                .build();
    }

    public List<InvestModelsDetails> getAllBaseValues() {
        return investModelDetailsRepo.findAll();
    }
}
