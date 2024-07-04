package dev.guarmo.whales.model.transaction.withdraw.mapper;

import dev.guarmo.whales.config.MapperConfig;
import dev.guarmo.whales.model.transaction.GetTransaction;
import dev.guarmo.whales.model.transaction.TransactionType;
import dev.guarmo.whales.model.transaction.withdraw.MoneyWithdraw;
import dev.guarmo.whales.model.transaction.withdraw.dto.GetWithdrawDto;
import dev.guarmo.whales.model.transaction.withdraw.dto.PostWithdrawDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface WithdrawMapper {
    GetWithdrawDto toGetDto(MoneyWithdraw moneyWithdraw);

    MoneyWithdraw toModel(PostWithdrawDto postWithdrawDto);

    @Mapping(target = "description", ignore = true)
    GetTransaction toGetTransaction(MoneyWithdraw withdraw);

    @AfterMapping
    default void setTransactionDesc(@MappingTarget GetTransaction getTransaction, MoneyWithdraw withdraw) {
        getTransaction.setTransactionType(TransactionType.WITHDRAW);
        String desc = withdraw.getCurrency()
                + " withdraw to " + withdraw.getCryptoAddress()
                + " [" + withdraw.getWithdrawStatus().name() + "]";
        getTransaction.setDescription(desc);
    }
}
