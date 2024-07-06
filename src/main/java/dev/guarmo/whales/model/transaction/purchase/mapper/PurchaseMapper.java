package dev.guarmo.whales.model.transaction.purchase.mapper;

import dev.guarmo.whales.config.MapperConfig;
import dev.guarmo.whales.helper.InvestModelHelper;
import dev.guarmo.whales.helper.UserHelper;
import dev.guarmo.whales.model.investmodel.InvestModel;
import dev.guarmo.whales.model.transaction.GetTransaction;
import dev.guarmo.whales.model.transaction.TransactionType;
import dev.guarmo.whales.model.transaction.income.mapper.IncomeMapperHelper;
import dev.guarmo.whales.model.transaction.purchase.Purchase;
import dev.guarmo.whales.model.transaction.purchase.dto.GetPurchaseDto;
import dev.guarmo.whales.model.transaction.purchase.dto.PostPurchaseDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface PurchaseMapper {
    GetPurchaseDto toGetDto(Purchase user);

    Purchase toModel(PostPurchaseDto crmUserDto);

    @Mapping(target = "description", ignore = true)
    GetTransaction toGetTransaction(Purchase purchase);
    @AfterMapping
    default void setTransactionDesc(@MappingTarget GetTransaction getTransaction, Purchase purchase) {
        getTransaction.setTransactionType(TransactionType.PURCHASE);
        getTransaction.setDescription("Bought " + InvestModelHelper.getNameByInvestModelLevel(purchase.getPurchasedModel()));
    }

    default Purchase getPurchaseBasedOnInvestModel(InvestModel investModel) {
        Purchase purchase = new Purchase();
        purchase.setPurchasedModel(investModel.getDetails().getInvestModelLevel());
        purchase.setTransactionAmount(investModel.getDetails().getPriceAmount());
        return purchase;
    }
}
