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
    @Mapping(target = "naming", source = "details.naming")
    @Mapping(target = "priceAmount", source = "details.priceAmount")
    @Mapping(target = "cyclesBeforeFreezeNumber", source = "details.cyclesBeforeFreezeNumber")
    @Mapping(target = "cyclesBeforeFinishedNumber", source = "details.cyclesBeforeFinishedNumber")
    @Mapping(target = "investModelLevel", source = "details.investModelLevel")
    GetInvestModel toGetDto(InvestModel investModel);
}
