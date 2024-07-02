package dev.guarmo.whales.controller;

import dev.guarmo.whales.model.transaction.deposit.dto.PostDepositDto;
import dev.guarmo.whales.model.transaction.deposit.mapper.DepositMapper;
import dev.guarmo.whales.service.DepositService;
import dev.guarmo.whales.teleg.TelegramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Objects;


@Slf4j
@RequiredArgsConstructor
@Controller
@CrossOrigin(allowedHeaders = "*")
public class InstantPayNotifyController {

    private final DepositService depositService;
    private final TelegramService telegramService;
    private final DepositMapper depositMapper;
    private static final String TRAN_FINISHED_STATUS = "completed";

    @PostMapping(value = "/pay/ipn", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<String> handlePaymentNotification(@RequestBody MultiValueMap<String, String> formData) {
        PostDepositDto postModel = depositMapper.toPostModel(formData);

        if (Objects.equals(postModel.getStatus(), TRAN_FINISHED_STATUS)) {
            depositService.linkDepositToUser(
                    postModel.getTransactionId());
        }
        log.info("PAYMENT RECEIVED: {}", postModel);
        telegramService.sendNotificationAboutSuccessTransaction(postModel);

        return ResponseEntity.ok("Thanks, notification received");
    }
}
