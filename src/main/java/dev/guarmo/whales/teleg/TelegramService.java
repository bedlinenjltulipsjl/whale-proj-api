package dev.guarmo.whales.teleg;

import dev.guarmo.whales.model.transaction.income.dto.GetIncomeDto;
import dev.guarmo.whales.model.transaction.invoice.dto.GetInvoiceDto;
import dev.guarmo.whales.model.transaction.deposit.dto.GetDepositDto;
import dev.guarmo.whales.model.user.UserCredentials;
import dev.guarmo.whales.model.transaction.withdraw.dto.GetWithdrawDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramService {
    private final Bot bot;
    @Value("${bot.notifchannel.id}")
    private Long DELIVERY_CHAT_ID;

    public void sendNotificationAboutWithdraw(UserCredentials userCredentials, GetWithdrawDto getWithdrawDto) {
        String text = userCredentials.getLogin() + "\n\n" + getWithdrawDto.toString();

        bot.sendMessageWithKeyboard(text, DELIVERY_CHAT_ID, String.valueOf(getWithdrawDto.getId()));
    }

    public void sendNotificationAboutStartingInvoice(GetInvoiceDto dto) {
        String text = dto.toString();

        bot.prepareAndSendMessage(DELIVERY_CHAT_ID, text);
    }

    public void sendNotificationAboutSuccessTransaction(GetDepositDto dto) {
        String text = dto.toString();

        bot.prepareAndSendMessage(DELIVERY_CHAT_ID, text);
    }

    public void sendNotificationAboutAssignedBonus(GetIncomeDto dto) {
        String text = dto.toString();
        bot.prepareAndSendMessage(DELIVERY_CHAT_ID, text);
    }

    public void sendNotificationAboutUserBuying(GetIncomeDto dto) {
        String text = dto.toString();
        bot.prepareAndSendMessage(DELIVERY_CHAT_ID, text);
    }
}
