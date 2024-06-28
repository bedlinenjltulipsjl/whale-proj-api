package dev.guarmo.whales.repository;

import dev.guarmo.whales.model.user.UserCredentials;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserCredentialsRepo extends CrudRepository<UserCredentials, String> {
    Optional<UserCredentials> findByLogin(String login);
    @Query("SELECT uc FROM UserCredentials uc " +
            "JOIN uc.incomes i " +
            "GROUP BY uc.id " +
            "ORDER BY SUM(i.transactionAmount) DESC")
    Page<UserCredentials> findTopByMaxSumOfIncomes(Pageable pageable);
}
