package dev.guarmo.whales.service;

import dev.guarmo.whales.helper.InvestModelHelper;
import dev.guarmo.whales.helper.UserHelper;
import dev.guarmo.whales.model.investmodel.InvestModel;
import dev.guarmo.whales.model.investmodel.InvestModelLevel;
import dev.guarmo.whales.model.investmodel.InvestModelStatus;
import dev.guarmo.whales.model.investmodel.dto.PostInvestModel;
import dev.guarmo.whales.model.transaction.income.IncomeType;
import dev.guarmo.whales.model.transaction.purchase.Purchase;
import dev.guarmo.whales.model.transaction.purchase.mapper.PurchaseMapper;
import dev.guarmo.whales.model.user.UserCredentials;
import dev.guarmo.whales.repository.InvestModelRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
                        .isReceivedBonus(false)
                        .investModelStatus(m.getDefaultStatus())
                        .build())
                .toList();
    }

    public InvestModel buyInvestModel(PostInvestModel investModelDto, String login) {
        UserCredentials model = userHelper.findByLoginModel(login);
        InvestModel investModelToBeBought = investModelHelper
                .findUserInvestModelByLevel(model, investModelDto.getInvestModelLevel());

        if (investModelToBeBought.getDetails().getPriceAmount() > model.getBalanceAmount()
        || investModelToBeBought.getInvestModelStatus() != InvestModelStatus.AVAILABLE) {

            String msg = String.format("Not enough balance [user balance: %s, model price: %s]. " +
                    "Or model status is not available for buying [status: %s]: ",
                    model.getBalanceAmount(),
                    investModelToBeBought.getDetails().getPriceAmount(),
                    investModelToBeBought.getInvestModelStatus());
            log.error(msg);
            throw new RuntimeException(msg);
        }

        Purchase unsavedPurchase = purchaseMapper
                .getPurchaseBasedOnInvestModel(investModelToBeBought);
        UserCredentials relinkedLostReferralBonuses = addLostReferralBonuses(model, investModelDto.getInvestModelLevel());
        UserCredentials withUpdatedModel = changeStatesAfterBuyingInvestEntity(relinkedLostReferralBonuses, investModelToBeBought);

        // Everything okay with balance. So link purchase, bonuses, invest tables and then save them
        purchaseService.linkAndSavePurchaseToUserAndBonusesToReferrals(
                unsavedPurchase, withUpdatedModel);

        return investModelToBeBought;
    }

    private UserCredentials addLostReferralBonuses(UserCredentials model, InvestModelLevel level) {
        model.getIncomes().stream()
                .filter(i -> i.getIncomeType().equals(IncomeType.LOST)
                        && i.getPurchasedModel().equals(level)
                ).forEach(i -> {
                    i.setIncomeType(IncomeType.REFERRAL);
                    i.setCreatedAt(LocalDateTime.now());
                    model.setBalanceAmount(
                            model.getBalanceAmount()
                                    + i.getTransactionAmount());
                });

        return model;
    }

    private UserCredentials changeStatesAfterBuyingInvestEntity(UserCredentials userCredentials,
                                                               InvestModel investModelToBeBought) {
        investModelToBeBought.setInvestModelStatus(InvestModelStatus.JUSTBOUGHT);
        investModelRepo.save(investModelToBeBought);
        return userCredentials;
        // DISABLE PREV FROZEN ELEMENT HERE, OR ALL FROZEN, DEPENDING ON WHAT ...
    }
}
