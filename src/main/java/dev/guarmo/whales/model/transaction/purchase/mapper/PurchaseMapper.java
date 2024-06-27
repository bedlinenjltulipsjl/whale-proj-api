package dev.guarmo.whales.model.transaction.purchase.mapper;

import dev.guarmo.whales.config.MapperConfig;
import dev.guarmo.whales.model.transaction.purchase.Purchase;
import dev.guarmo.whales.model.transaction.purchase.dto.GetPurchaseDto;
import dev.guarmo.whales.model.transaction.purchase.dto.PostPurchaseDto;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface PurchaseMapper {
    GetPurchaseDto toGetDto(Purchase user);

    Purchase toModel(PostPurchaseDto crmUserDto);
}
