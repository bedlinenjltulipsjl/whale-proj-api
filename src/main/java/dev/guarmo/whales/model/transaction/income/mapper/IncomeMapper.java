package dev.guarmo.whales.model.transaction.income.mapper;

import dev.guarmo.whales.config.MapperConfig;
import dev.guarmo.whales.model.transaction.GetTransaction;
import dev.guarmo.whales.model.transaction.TransactionType;
import dev.guarmo.whales.model.transaction.income.Income;
import dev.guarmo.whales.model.transaction.income.dto.GetIncomeDto;
import dev.guarmo.whales.model.transaction.income.dto.PostIncomeDto;
import dev.guarmo.whales.model.user.UserCredentials;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = IncomeMapperHelper.class)
public interface IncomeMapper {
    GetIncomeDto toGetDto(Income income);

    @Mapping(target = "incomeCausedByUser", source = "incomeCausedByUserTgName", qualifiedByName = "createUserFromLogin")
    Income toModel(PostIncomeDto postIncomeDto);

    @Mapping(target = "description", ignore = true)
    GetTransaction toGetTransaction(Income deposit);
    @AfterMapping
    default void setTransactionDesc(@MappingTarget GetTransaction getTransaction, Income income) {
        getTransaction.setTransactionType(TransactionType.INCOME);
        getTransaction.setDescription("Referral bonus from " + income.getIncomeCausedByUser().getName());
    }

    default Income createBonusWithSenderAndAmount(Double amount, UserCredentials incomeCausedByUser) {
        Income income = new Income();
        income.setIncomeCausedByUser(incomeCausedByUser);
        income.setTransactionAmount(amount);
        return income;
    }
}
