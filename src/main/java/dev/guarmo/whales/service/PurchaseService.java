package dev.guarmo.whales.service;

import dev.guarmo.whales.model.transaction.purchase.Purchase;
import dev.guarmo.whales.model.transaction.purchase.dto.GetPurchaseDto;
import dev.guarmo.whales.model.transaction.purchase.dto.PostPurchaseDto;
import dev.guarmo.whales.model.transaction.purchase.mapper.PurchaseMapper;
import dev.guarmo.whales.model.user.UserCredentials;
import dev.guarmo.whales.repository.PurchaseRepo;
import dev.guarmo.whales.repository.UserCredentialsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseMapper purchaseMapper;
    private final PurchaseRepo purchaseRepo;
    private final UserCredentialsRepo userCredentialsRepo;

    public List<GetPurchaseDto> getPurchaseDtoList(String tgid) {
        return userCredentialsRepo.findByLogin(tgid).orElseThrow()
                .getPurchases()
                .stream()
                .map(purchaseMapper::toGetDto)
                .toList();
    }

    public List<Purchase> getPurchaseModelsByUser(String tgid) {
        return userCredentialsRepo.findByLogin(tgid)
                .orElseThrow()
                .getPurchases();
    }

    @Transactional
    public GetPurchaseDto addPurchaseToUser(PostPurchaseDto dto, String tgid) {
        Purchase model = purchaseMapper.toModel(dto);
        Purchase saved = purchaseRepo.save(model);

        UserCredentials byLoginModel = userCredentialsRepo
                .findByLogin(tgid)
                .orElseThrow();
        byLoginModel.getPurchases().add(saved);

        byLoginModel.setBalanceAmount(
                byLoginModel.getBalanceAmount() - saved.getTransactionAmount());

        userCredentialsRepo.save(byLoginModel);
        return purchaseMapper.toGetDto(saved);

    }
}
