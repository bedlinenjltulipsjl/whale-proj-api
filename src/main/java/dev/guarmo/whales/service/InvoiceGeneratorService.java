package dev.guarmo.whales.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.guarmo.whales.model.transaction.invoice.Invoice;
import dev.guarmo.whales.model.transaction.invoice.dto.GetInvoiceDto;
import dev.guarmo.whales.model.transaction.invoice.mapper.InvoiceMapper;
import dev.guarmo.whales.repository.InvoiceRepo;
import dev.guarmo.whales.teleg.TelegramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceGeneratorService {
    private final InvoiceMapper invoiceMapper;
    private final InvoiceRepo invoiceRepository;
    private final WestWalletService westWalletService;

    public GetInvoiceDto generateInvoicePageObject(String userLogin, String cryptoCurrencyType, Double topUpAmount, String email) {
        String string = westWalletService.generateInvoicePagePostRequestResponse(userLogin, cryptoCurrencyType, topUpAmount);
        try {
            Invoice invoice = invoiceMapper.getInvoiceFromJson(string);

            // We are passing label and email to request, but they are not returned
            // As response from request, so we need to after init them here once again
            invoice.setLabel(userLogin);
            invoice.setEmail(email);

            return invoiceMapper.toGetDto(invoiceRepository.save(invoice));
        } catch (JsonProcessingException e) {
            log.error("Error while parsing json", e);
            throw new RuntimeException("Error while parsing json", e);
        }
    }
}
