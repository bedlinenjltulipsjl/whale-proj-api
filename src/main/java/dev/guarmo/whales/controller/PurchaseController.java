package dev.guarmo.whales.controller;

import dev.guarmo.whales.model.transaction.purchase.dto.GetPurchaseDto;
import dev.guarmo.whales.model.transaction.purchase.dto.PostPurchaseDto;
import dev.guarmo.whales.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/purchases")
public class PurchaseController {
    private final PurchaseService purchaseService;

    @GetMapping("/{tgid}")
    public List<GetPurchaseDto> getPurchaseDtoList(@PathVariable String tgid) {
        return purchaseService.getPurchaseDtoList(tgid);
    }

    @PostMapping("/{tgid}")
    public GetPurchaseDto addPurchaseToUser(@RequestBody PostPurchaseDto purchase, @PathVariable String tgid) {
        return purchaseService.addPurchaseToUser(purchase, tgid);
    }
}
