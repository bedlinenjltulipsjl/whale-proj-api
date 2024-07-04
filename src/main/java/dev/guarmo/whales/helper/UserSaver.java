package dev.guarmo.whales.helper;

import dev.guarmo.whales.model.user.UserCredentials;
import dev.guarmo.whales.repository.UserCredentialsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSaver {
    private final UserCredentialsRepo userCredentialsRepo;

    public List<UserCredentials> saveAll(List<UserCredentials> userCredentials) {
        Iterable<UserCredentials> userCredentials1 = userCredentialsRepo.saveAll(userCredentials);
        List<UserCredentials> listCredentials = new ArrayList<>();
        userCredentials1.forEach(listCredentials::add);
        return listCredentials;
    }

    public UserCredentials save(UserCredentials userCredentials) {
        return userCredentialsRepo.save(userCredentials);
    }
}
