package dev.guarmo.whales.repository;

import dev.guarmo.whales.model.transaction.income.Income;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeRepo extends JpaRepository<Income, Long> {
}
