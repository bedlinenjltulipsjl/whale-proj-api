package dev.guarmo.whales.service;

import dev.guarmo.whales.helper.InvestModelHelper;
import dev.guarmo.whales.helper.UserHelper;
import dev.guarmo.whales.model.investmodel.InvestModel;
import dev.guarmo.whales.model.investmodel.InvestModelLevel;
import dev.guarmo.whales.model.investmodel.InvestModelStatus;
import dev.guarmo.whales.model.investmodel.dto.PostInvestModel;
import dev.guarmo.whales.model.investmodel.mapper.InvestModelMapper;
import dev.guarmo.whales.model.transaction.purchase.Purchase;
import dev.guarmo.whales.model.transaction.purchase.mapper.PurchaseMapper;
import dev.guarmo.whales.model.user.UserCredentials;
import dev.guarmo.whales.repository.InvestModelRepo;
import dev.guarmo.whales.repository.UserCredentialsRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class InvestModelService {
    private final InvestModelRepo investModelRepo;
    private final PurchaseService purchaseService;
    private final UserHelper userHelper;
    private final InvestModelHelper investModelHelper;
    private final PurchaseMapper purchaseMapper;
    private final InvestModelDetailsService investModelDetailsService;


    public List<InvestModel> generateDefaultInvestModels() {
        List<InvestModel> investModels = getInvestModelsList();
        return investModelRepo.saveAll(investModels);
    }

    public List<InvestModel> getInvestModelsList() {
        return investModelDetailsService.getAllBaseValues()
                .stream()
                .map(m -> InvestModel
                        .builder()
                        .details(m)
                        .cyclesCount(0)
                        .investModelStatus(m.getDefaultStatus())
                        .build())
                .toList();

//        InvestModel[] investModels = new InvestModel[15];
//
//        for (int i = 0; i < 15; i++) {
//            investModels[i] = generateInvestModel(i);
//        }
//
//        List<InvestModel> list = Arrays.stream(investModels)
//                .sorted(Comparator.comparing(e -> e.getDetails().getInvestModelLevel()))
//                .toList();

//        list.get(0).setInvestModelStatus(InvestModelStatus.AVAILABLE);
//        list.get(1).setInvestModelStatus(InvestModelStatus.MONEYLOCKED);
//        list.get(1).setInvestModelStatus(InvestModelStatus.MONEYLOCKED);
//        investModels[2].setInvestModelStatus(InvestModelStatus.TIMELOCKED);
//        investModels[2].setUnlockDate(LocalDateTime.now().plusDays(1).minusHours(3)); // Example cycles before freeze count

//        list.get(12).setInvestModelStatus(InvestModelStatus.SPECIALS);
//        list.get(13).setInvestModelStatus(InvestModelStatus.SPECIALS);
//        list.get(14).setInvestModelStatus(InvestModelStatus.SPECIALS);
    }

//    private InvestModel generateInvestModel(int i) {
//        InvestModel investModel = new InvestModel();
//        investModel.setDetails()
//
//        investModel.setPriceAmount(100.0 * (i + 1)); // Example price calculation
//        investModel.setCyclesCount(0); // Example cycles count
//        investModel.setCyclesBeforeFinishedNumber(i + 19); // Example cycles before freeze count
//        investModel.setCyclesBeforeFreezeNumber(i + 4); // Example cycles before freeze count
//        investModel.setInvestModelLevel(InvestModelLevel.values()[i]); // Assigning levels sequentially
//
//        // Assigning statuses in increasing order from 1 to 16
//        investModel.setInvestModelStatus(InvestModelStatus.LOCKED);
//        return investModel;
//    }

    public InvestModel buyInvestModel(PostInvestModel investModelDto, String login) {
        UserCredentials model = userHelper.findByLoginModel(login);
        InvestModel investModel = investModelHelper
                .findUserInvestModelByLevel(model, investModelDto.getInvestModelLevel());
        Purchase unsavedPurchase = purchaseMapper
                .getPurchaseBasedOnInvestModel(investModel);

        if (investModel.getDetails().getPriceAmount() > model.getBalanceAmount()
        || investModel.getInvestModelStatus() != InvestModelStatus.AVAILABLE) {

            String msg = String.format("Not enough balance [user balance: %s, model price: %s]. " +
                    "Or model status is not available for buying [status: %s]: ",
                    model.getBalanceAmount(),
                    investModel.getDetails().getPriceAmount(),
                    investModel.getInvestModelStatus());
            log.error(msg);
            throw new RuntimeException(msg);
        }

        // Everything okay with balance. So link purchase, bonuses, invest tables and then save them
        purchaseService.linkAndSavePurchaseToUserAndBonusesToReferrals(
                        unsavedPurchase, investModelHelper.linkNewInvestEntities(model));

        return investModel;
    }

//    private InvestModel checkStatusesToChange(InvestModel investModel) {
//        if (investModel.getInvestModelStatus().equals(InvestModelStatus.TIMELOCKED)
//        && investModel.getUnlockDate().isAfter(investModel.getUnlockDate())) {
//            investModel.setInvestModelStatus(InvestModelStatus.AVAILABLE);
//        }
//    }
}
