package dev.guarmo.whales.service;

import dev.guarmo.whales.helper.InvestModelHelper;
import dev.guarmo.whales.helper.UserHelper;
import dev.guarmo.whales.model.investmodel.InvestModel;
import dev.guarmo.whales.model.investmodel.InvestModelLevel;
import dev.guarmo.whales.model.investmodel.InvestModelStatus;
import dev.guarmo.whales.model.investmodel.dto.GetInvestModel;
import dev.guarmo.whales.model.investmodel.dto.PostInvestModel;
import dev.guarmo.whales.model.investmodel.mapper.InvestModelMapper;
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
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class InvestModelService {
    private final InvestModelRepo investModelRepo;
    private final PurchaseService purchaseService;
    private final UserHelper userHelper;
    private final InvestModelMapper investModelMapper;
    private final InvestModelHelper investModelHelper;



    public List<InvestModel> generateDefaultInvestModels() {
        List<InvestModel> investModels = getInvestModelsList();
        return investModelRepo.saveAll(investModels);
    }

    public List<InvestModel> getInvestModelsList() {
        InvestModel[] investModels = new InvestModel[15];

        for (int i = 0; i < 15; i++) {
            investModels[i] = generateInvestModel(i);
        }

        investModels[0].setInvestModelStatus(InvestModelStatus.AVAILABLE);
        investModels[1].setInvestModelStatus(InvestModelStatus.MONEYLOCKED);
//        investModels[2].setInvestModelStatus(InvestModelStatus.TIMELOCKED);
//        investModels[2].setUnlockDate(LocalDateTime.now().plusDays(1).minusHours(3)); // Example cycles before freeze count

        investModels[12].setInvestModelStatus(InvestModelStatus.SPECIALS);
        investModels[13].setInvestModelStatus(InvestModelStatus.SPECIALS);
        investModels[14].setInvestModelStatus(InvestModelStatus.SPECIALS);

        return Arrays.stream(investModels)
                .sorted(Comparator.comparing(InvestModel::getInvestModelLevel))
                .toList();
    }

    private InvestModel generateInvestModel(int i) {
        InvestModel investModel = new InvestModel();
        investModel.setNaming("InvestModel " + i);
        investModel.setPriceAmount(100.0 * (i + 1)); // Example price calculation
        investModel.setCyclesCount(0); // Example cycles count
        investModel.setCyclesBeforeFinishedNumber(i + 19); // Example cycles before freeze count
        investModel.setCyclesBeforeFreezeNumber(i + 4); // Example cycles before freeze count
        investModel.setInvestModelLevel(InvestModelLevel.values()[i]); // Assigning levels sequentially

        // Assigning statuses in increasing order from 1 to 16
        investModel.setInvestModelStatus(InvestModelStatus.LOCKED);
        return investModel;
    }

    @Transactional
    public GetInvestModel buyInvestModel(PostInvestModel investModel, String login) {
        UserCredentials model = userHelper.findByLoginModel(login);
        InvestModel gotModelFromUser = model.getInvestModels().stream().filter(im -> im.getInvestModelLevel() == investModel.getInvestModelLevel()).findFirst().orElseThrow();

        if ((gotModelFromUser.getUnlockDate().isBefore(LocalDateTime.now())
        && gotModelFromUser.getInvestModelStatus() == InvestModelStatus.TIMELOCKED)
        || gotModelFromUser.getInvestModelStatus() == InvestModelStatus.AVAILABLE) {

            PostPurchaseDto postPurchaseDto = new PostPurchaseDto();
            postPurchaseDto.setPurchasedModel(gotModelFromUser.getInvestModelLevel());
            postPurchaseDto.setTransactionAmount(gotModelFromUser.getPriceAmount());
            purchaseService.addPurchaseToUser(postPurchaseDto, model);

            model = investModelHelper.updateInvestEntities(model);
            return investModelMapper.toGetDto(investModelRepo.save(gotModelFromUser));
        } else {
            log.error("Invest Model wrong status (UNABLE TO PURCHASE): {}", gotModelFromUser);
            throw new RuntimeException("Invest Model wrong status (UNABLE TO PURCHASE): " + gotModelFromUser);
        }
    }

    public List<GetInvestModel> getAllInvestModels(String name) {
        UserCredentials model = userHelper.findByLoginModel(name);
        model.getInvestModels().stream();
        return null;
    }

//    private InvestModel checkStatusesToChange(InvestModel investModel) {
//        if (investModel.getInvestModelStatus().equals(InvestModelStatus.TIMELOCKED)
//        && investModel.getUnlockDate().isAfter(investModel.getUnlockDate())) {
//            investModel.setInvestModelStatus(InvestModelStatus.AVAILABLE);
//        }
//    }
}
