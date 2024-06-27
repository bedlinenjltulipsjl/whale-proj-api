package dev.guarmo.whales.repository;

import dev.guarmo.whales.model.transaction.deposit.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepositRepo extends JpaRepository<Deposit, Long> {
}
