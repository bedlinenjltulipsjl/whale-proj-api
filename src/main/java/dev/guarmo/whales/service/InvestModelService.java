package dev.guarmo.whales.service;

import dev.guarmo.whales.helper.UserHelper;
import dev.guarmo.whales.model.investmodel.InvestModel;
import dev.guarmo.whales.model.investmodel.InvestModelLevel;
import dev.guarmo.whales.model.investmodel.InvestModelStatus;
import dev.guarmo.whales.model.investmodel.dto.GetInvestModel;
import dev.guarmo.whales.model.investmodel.dto.PostInvestModel;
import dev.guarmo.whales.model.transaction.purchase.Purchase;
import dev.guarmo.whales.model.transaction.purchase.dto.PostPurchaseDto;
import dev.guarmo.whales.model.user.UserCredentials;
import dev.guarmo.whales.repository.InvestModelRepo;
import dev.guarmo.whales.repository.UserCredentialsRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.SplittableRandom;

@RequiredArgsConstructor
@Service
@Slf4j
public class InvestModelService {
    private final InvestModelRepo investModelRepo;
    private final PurchaseService purchaseService;
    private final UserHelper userHelper;
    private final UserCredentialsRepo userCredentialsRepo;

    public List<InvestModel> generateDefaultInvestModels() {
        List<InvestModel> investModels = getInvestModelsList();
        return investModelRepo.saveAll(investModels);
    }

    public List<InvestModel> getInvestModelsList() {
        InvestModel[] investModels = new InvestModel[15];

        for (int i = 0; i < 15; i++) {
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
        investModel.setCyclesBeforeFinishedNumber(i + 19); // Example cycles before freeze count
        investModel.setCyclesBeforeFreezeNumber(i + 4); // Example cycles before freeze count
        investModel.setUnlockDate(LocalDateTime.now().minusHours(i)); // Example cycles before freeze count
        investModel.setInvestModelLevel(InvestModelLevel.values()[i]); // Assigning levels sequentially

        // Assigning statuses in increasing order from 1 to 16
        investModel.setInvestModelStatus(InvestModelStatus.TIMELOCKED);
        return investModel;
    }

    @Transactional
    public GetInvestModel addInvestModel(PostInvestModel investModel, String login) {
        UserCredentials model = userCredentialsRepo.findByLogin(login).orElseThrow();
        InvestModel gotModelFromUser = model.getInvestModels().stream().filter(im -> im.getInvestModelLevel() == investModel.getInvestModelLevel()).findFirst().orElseThrow();
        if (gotModelFromUser.getInvestModelStatus().equals(InvestModelStatus.AVAILABLE)) {
            PostPurchaseDto postPurchaseDto = new PostPurchaseDto();
            postPurchaseDto.setPurchasedModel(gotModelFromUser.getInvestModelLevel());
            postPurchaseDto.setTransactionAmount(gotModelFromUser.getPriceAmount());
            purchaseService.addPurchaseToUser(postPurchaseDto, login);

            gotModelFromUser.setInvestModelStatus(InvestModelStatus.BOUGHT);
            investModelRepo.save(gotModelFromUser);
        }
        log.error("Invest Model wrong status (UNABLE TO PURCHASE): {}", gotModelFromUser);
        throw new RuntimeException("Invest Model wrong status (UNABLE TO PURCHASE): " + gotModelFromUser);
    }

//    @Transactional


//    public List<GetInvestModel> getAllInvestTables(String name) {
//
//    }
}
