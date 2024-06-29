package dev.guarmo.whales.controller;

import dev.guarmo.whales.model.transaction.purchase.dto.GetPurchaseDto;
import dev.guarmo.whales.model.transaction.purchase.dto.PostPurchaseDto;
import dev.guarmo.whales.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/purchases")
public class PurchaseController {
    private final PurchaseService purchaseService;

    @GetMapping
    public List<GetPurchaseDto> getPurchaseDtoList(Authentication authentication) {
        return purchaseService.getPurchaseDtoList(authentication.getName());
    }

//    @PostMapping
//    public GetPurchaseDto addPurchaseToUser(@RequestBody PostPurchaseDto purchase, Authentication authentication) {
//        return purchaseService.addPurchaseToUser(purchase, authentication.getName());
//    }
}
