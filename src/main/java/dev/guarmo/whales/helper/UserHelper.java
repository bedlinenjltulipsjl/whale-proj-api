package dev.guarmo.whales.helper;

import dev.guarmo.whales.model.user.UserCredentials;
import dev.guarmo.whales.repository.UserCredentialsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserHelper {
    private final UserCredentialsRepo userCredentialsRepo;

    public UserCredentials findByLoginModel(String tgid) {
        return userCredentialsRepo.findByLogin(tgid).orElseThrow();
    }
}
