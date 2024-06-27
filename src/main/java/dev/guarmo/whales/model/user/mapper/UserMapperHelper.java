package dev.guarmo.whales.model.user.mapper;

import dev.guarmo.whales.model.user.UserCredentials;
import dev.guarmo.whales.repository.UserCredentialsRepo;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapperHelper {

    private final UserCredentialsRepo userCredentialsRepo;

    @Named("mapUpperReferralByLogin")
    public UserCredentials mapUpperReferralByLogin(String login) {
        return userCredentialsRepo.findByLogin(login).orElseThrow();
    }

    @Named("mapUpperReferralToLogin")
    public String mapUpperReferralByLogin(UserCredentials userCredentials) {
        return userCredentials.getLogin();
    }
}