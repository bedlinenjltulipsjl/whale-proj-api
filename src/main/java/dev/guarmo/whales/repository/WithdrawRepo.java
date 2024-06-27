package dev.guarmo.whales.repository;

import dev.guarmo.whales.model.transaction.withdraw.MoneyWithdraw;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawRepo extends JpaRepository<MoneyWithdraw, Long> {
}
