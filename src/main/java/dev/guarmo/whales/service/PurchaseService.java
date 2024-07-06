package dev.guarmo.whales.service;

import dev.guarmo.whales.helper.UserSaver;
import dev.guarmo.whales.model.transaction.purchase.Purchase;
import dev.guarmo.whales.model.user.UserCredentials;
import dev.guarmo.whales.repository.PurchaseRepo;
import dev.guarmo.whales.teleg.TelegramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseService {
    private final PurchaseRepo purchaseRepo;
    private final IncomeService incomeService;
    private final ReferralBonusService referralBonusService;
    private final UserSaver userSaver;
    private final TelegramService telegramService;

    public void linkAndSavePurchaseToUserAndBonusesToReferrals(
            Purchase purchase, UserCredentials userModel) {

        UserCredentials modelWithLinkedPurchase = linkPurchaseToUser(purchase, userModel);
        userSaver.save(modelWithLinkedPurchase);

        List<UserCredentials> threeUsersWithLinkedBonuses = incomeService.linkBonusToUpperReferrals(
                purchase.getTransactionAmount(),
                purchase.getPurchasedModel(),
                modelWithLinkedPurchase);
        userSaver.saveAll(threeUsersWithLinkedBonuses);

        UserCredentials userWithMainBonusLinked = referralBonusService.linkMainBonusToRandomReferral(
                purchase.getTransactionAmount(),
                purchase.getPurchasedModel(),
                userModel);
        userSaver.save(userWithMainBonusLinked);
    }

    public UserCredentials linkPurchaseToUser(Purchase model, UserCredentials byLoginModel) {
        Purchase saved = purchaseRepo.save(model);

        byLoginModel.getPurchases().add(saved);
        byLoginModel.setBalanceAmount(
                byLoginModel.getBalanceAmount()
                        - saved.getTransactionAmount());
//        byLoginModel.getPurchases().add(model);
//        byLoginModel.setBalanceAmount(
//                byLoginModel.getBalanceAmount()
//                        - model.getTransactionAmount());
        String msg = String.format("Purchase from %s that causes income:\n\nPurchase: %s", byLoginModel, saved);
        log.info(msg);
        telegramService.sendTextNotification(msg);
        return byLoginModel;
    }
}
