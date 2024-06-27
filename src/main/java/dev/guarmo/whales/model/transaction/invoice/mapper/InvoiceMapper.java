package dev.guarmo.whales.model.transaction.invoice.mapper;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.guarmo.whales.config.MapperConfig;
import dev.guarmo.whales.model.transaction.invoice.Invoice;
import dev.guarmo.whales.model.transaction.invoice.dto.GetInvoiceDto;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface InvoiceMapper {
    String PATTERN_TO_REMOVE_FROM_JSON_STRING = "\"allowed_currencies_data\":\\[\\{";
    String PATTERN_2_TO_REMOVE_FROM_JSON_STRING = "}]";

    default Invoice getInvoiceFromJson(final String invoiceAsUnmodifiedJson) throws JsonProcessingException {
        String modifiedJson = modifyJsonString(invoiceAsUnmodifiedJson);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(modifiedJson, Invoice.class);
    }

    default String modifyJsonString(final String invoiceAsUnmodifiedJson) {
        String modified = invoiceAsUnmodifiedJson.replaceAll(PATTERN_TO_REMOVE_FROM_JSON_STRING, "");
        return modified.replaceAll(PATTERN_2_TO_REMOVE_FROM_JSON_STRING, "");
    }

    GetInvoiceDto toGetDto(Invoice user);
}
