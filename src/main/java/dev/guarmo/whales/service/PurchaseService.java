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

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseMapper purchaseMapper;
    private final PurchaseRepo purchaseRepo;
    private final UserService userService;
    private final UserCredentialsRepo userCredentialsRepo;

    public List<GetPurchaseDto> getPurchaseDtoList(String tgid) {
        return userService.findByLoginModel(tgid)
                .getPurchases()
                .stream()
                .map(purchaseMapper::toGetDto)
                .toList();
    }

    public GetPurchaseDto addPurchaseToUser(PostPurchaseDto dto, String tgid) {
        Purchase model = purchaseRepo.save(purchaseMapper.toModel(dto));

        UserCredentials byLoginModel = userService.findByLoginModel(tgid);
        byLoginModel.getPurchases().add(model);

        userCredentialsRepo.save(byLoginModel);
        return purchaseMapper.toGetDto(model);

    }
}
