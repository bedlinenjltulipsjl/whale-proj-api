package dev.guarmo.whales.model.investmodel.mapper;

import dev.guarmo.whales.config.MapperConfig;
import dev.guarmo.whales.model.investmodel.InvestModel;
import dev.guarmo.whales.model.investmodel.dto.GetInvestModel;
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
public interface InvestModelMapper {
    GetInvestModel toGetDto(InvestModel investModel);

//    MoneyWithdraw toModel(PostWithdrawDto postWithdrawDto);
}
