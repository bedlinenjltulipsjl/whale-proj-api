package dev.guarmo.whales.model.transaction.deposit.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.guarmo.whales.config.MapperConfig;
import dev.guarmo.whales.model.transaction.GetTransaction;
import dev.guarmo.whales.model.transaction.TransactionType;
import dev.guarmo.whales.model.transaction.deposit.Deposit;
import dev.guarmo.whales.model.transaction.deposit.dto.GetDepositDto;
import dev.guarmo.whales.model.transaction.deposit.dto.PostDepositDto;
import org.checkerframework.checker.units.qual.A;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.util.MultiValueMap;

@Mapper(config = MapperConfig.class)
public interface DepositMapper {
    default PostDepositDto toPostModel(MultiValueMap<String, String> formData) {
        PostDepositDto notification = new PostDepositDto();
        notification.setTransactionId(Long.parseLong(formData.getFirst("id")));
        notification.setTransactionAmount(Double.parseDouble(formData.getFirst("amount")));
        notification.setAddress(formData.getFirst("address"));
        notification.setDestTag(formData.getFirst("dest_tag"));
        notification.setLabel(formData.getFirst("label"));
        notification.setCurrency(formData.getFirst("currency"));
        notification.setStatus(formData.getFirst("status"));
        notification.setBlockchainConfirmations(Integer.parseInt(formData.getFirst("blockchain_confirmations")));
        notification.setFee(formData.getFirst("fee"));
        notification.setBlockchainHash(formData.getFirst("blockchain_hash"));
        return notification;
    }

    default PostDepositDto fromJsonToPost(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, PostDepositDto.class);
    }

    GetDepositDto toGetDto(Deposit user);

    Deposit toModel(PostDepositDto crmUserDto);

    @Mapping(target = "description", ignore = true)
    GetTransaction toGetTransaction(Deposit deposit);
    @AfterMapping
    default void setTransactionDesc(@MappingTarget GetTransaction getTransaction, Deposit deposit) {
        getTransaction.setTransactionType(TransactionType.DEPOSIT);
        getTransaction.setDescription(deposit.getCurrency() + " deposit from address " + deposit.getAddress());
    }
}
