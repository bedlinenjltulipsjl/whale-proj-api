package dev.guarmo.whales.helper;

import dev.guarmo.whales.model.user.UserCredentials;
import dev.guarmo.whales.repository.UserCredentialsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor
public class UserHelper {
    private final UserCredentialsRepo userCredentialsRepo;

    public UserCredentials findByLoginModel(String tgid) {
        return userCredentialsRepo.findByLogin(tgid).orElseThrow();
    }

    public List<UserCredentials> findAllUsers() {
        return StreamSupport.stream(
                userCredentialsRepo.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }
}
