package dev.guarmo.whales.model.transaction.income.mapper;

import dev.guarmo.whales.model.investmodel.InvestModel;
import dev.guarmo.whales.model.investmodel.InvestModelLevel;
import dev.guarmo.whales.model.user.UserCredentials;
import dev.guarmo.whales.repository.UserCredentialsRepo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class IncomeMapperHelper {
    private final UserCredentialsRepo userCredentialsRepo;

    @Named("createUserFromLogin")
    public UserCredentials createUserFromLogin(final String userLogin) {
        return userCredentialsRepo.findByLogin(userLogin).orElseThrow();
    }
}
