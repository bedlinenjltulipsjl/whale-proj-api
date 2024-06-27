package dev.guarmo.whales.model.transaction.income.mapper;

import dev.guarmo.whales.model.user.UserCredentials;
import dev.guarmo.whales.repository.UserCredentialsRepo;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IncomeMapperHelper {
    private final UserCredentialsRepo userCredentialsRepo;

    @Named("createUserFromLogin")
    public UserCredentials createUserFromLogin(final String userLogin) {
        return userCredentialsRepo.findByLogin(userLogin).orElseThrow();
    }
}
