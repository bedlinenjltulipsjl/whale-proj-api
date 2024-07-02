package dev.guarmo.whales.controller;

import dev.guarmo.whales.model.currency.CryptoCurrency;
import dev.guarmo.whales.service.CryptoConvertService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/convert")
public class CryptoConvertController {
    private final CryptoConvertService cryptoConvertService;

    @PutMapping
    public List<CryptoCurrency> updateAll() {
        cryptoConvertService.fillWithBaseData();
        return cryptoConvertService.updateCurrenciesTable();
    }
}
