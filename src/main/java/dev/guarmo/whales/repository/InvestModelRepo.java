package dev.guarmo.whales.repository;

import dev.guarmo.whales.model.investmodel.InvestModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvestModelRepo extends JpaRepository<InvestModel, Long> {
}
