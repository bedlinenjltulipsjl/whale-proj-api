package dev.guarmo.whales.repository;

import dev.guarmo.whales.model.currency.CryptoCurrency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CryptoCurrencyRepo extends JpaRepository<CryptoCurrency, Long> {
    Optional<CryptoCurrency> findCryptoCurrencyByType(String type);
}
