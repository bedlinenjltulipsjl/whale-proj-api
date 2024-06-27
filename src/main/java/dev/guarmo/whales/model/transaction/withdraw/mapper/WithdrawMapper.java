package dev.guarmo.whales.model.transaction.withdraw.mapper;

import dev.guarmo.whales.config.MapperConfig;
import dev.guarmo.whales.model.transaction.withdraw.MoneyWithdraw;
import dev.guarmo.whales.model.transaction.withdraw.dto.GetWithdrawDto;
import dev.guarmo.whales.model.transaction.withdraw.dto.PostWithdrawDto;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface WithdrawMapper {
    GetWithdrawDto toGetDto(MoneyWithdraw moneyWithdraw);

    MoneyWithdraw toModel(PostWithdrawDto postWithdrawDto);
}
