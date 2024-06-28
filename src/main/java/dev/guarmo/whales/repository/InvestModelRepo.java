package dev.guarmo.whales.repository;

import dev.guarmo.whales.model.investmodel.InvestModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestModelRepo extends JpaRepository<InvestModel, Long> {
}
