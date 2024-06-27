package dev.guarmo.whales.repository;

import dev.guarmo.whales.model.user.UserCredentials;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserCredentialsRepo extends CrudRepository<UserCredentials, String> {
    Optional<UserCredentials> findByLogin(String login);
}
