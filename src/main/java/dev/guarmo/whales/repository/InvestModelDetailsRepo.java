package dev.guarmo.whales.repository;

import dev.guarmo.whales.model.investmodel.InvestModelsDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestModelDetailsRepo extends JpaRepository<InvestModelsDetails, Long> {
}
