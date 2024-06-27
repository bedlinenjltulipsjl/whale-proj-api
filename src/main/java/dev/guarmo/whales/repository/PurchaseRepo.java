package dev.guarmo.whales.repository;

import dev.guarmo.whales.model.transaction.purchase.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepo extends JpaRepository<Purchase, Long> {
}
