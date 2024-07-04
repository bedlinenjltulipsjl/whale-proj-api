package dev.guarmo.whales.controller;

import dev.guarmo.whales.model.currency.CryptoCurrency;
import dev.guarmo.whales.model.investmodel.InvestModelsDetails;
import dev.guarmo.whales.service.CryptoConvertService;
import dev.guarmo.whales.service.InvestModelDetailsService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tables/details")
@CrossOrigin(allowedHeaders = "*")
public class InvestEntityDetailsController {
    private final InvestModelDetailsService investModelDetailsService;

    @PostMapping
    public List<InvestModelsDetails> saveAllBaseValues() {
        return investModelDetailsService.saveBaseValues();
    }

    @GetMapping
    public List<InvestModelsDetails> getAllBaseValues() {
        return investModelDetailsService.getAllBaseValues();
    }
}
