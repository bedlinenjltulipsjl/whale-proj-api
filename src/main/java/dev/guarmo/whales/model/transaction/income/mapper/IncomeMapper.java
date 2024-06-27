package dev.guarmo.whales.model.transaction.income.mapper;

import dev.guarmo.whales.config.MapperConfig;
import dev.guarmo.whales.model.transaction.income.Income;
import dev.guarmo.whales.model.transaction.income.dto.GetIncomeDto;
import dev.guarmo.whales.model.transaction.income.dto.PostIncomeDto;
import dev.guarmo.whales.model.user.UserCredentials;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = IncomeMapperHelper.class)
public interface IncomeMapper {
    GetIncomeDto toGetDto(Income income);

    @Mapping(target = "incomeCausedByUser", source = "incomeCausedByUserTgName", qualifiedByName = "createUserFromLogin")
    Income toModel(PostIncomeDto postIncomeDto);

    default Income createBonusWithSenderAndAmount(Double amount, UserCredentials incomeCausedByUser) {
        Income income = new Income();
        income.setIncomeCausedByUser(incomeCausedByUser);
        income.setTransactionAmount(amount);
        return income;
    }
}
