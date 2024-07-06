package dev.guarmo.whales.model.investmodel.mapper;

import dev.guarmo.whales.config.MapperConfig;
import dev.guarmo.whales.model.investmodel.InvestModel;
import dev.guarmo.whales.model.investmodel.dto.GetInvestModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = {InvestModelMapper.class})
public interface InvestModelMapper {
    @Mapping(target = "naming", source = "details.naming")
    @Mapping(target = "priceAmount", source = "details.priceAmount")
    @Mapping(target = "cyclesBeforeFinishedNumber", source = "details.cyclesBeforeFinishedNumber")
    @Mapping(target = "cyclesCount", source = "details.cyclesCount")
    @Mapping(target = "investModelLevel", source = "details.investModelLevel")
    @Mapping(target = "unlockDate", source = "details.unlockDate")
    GetInvestModel toGetDto(InvestModel investModel);
}
