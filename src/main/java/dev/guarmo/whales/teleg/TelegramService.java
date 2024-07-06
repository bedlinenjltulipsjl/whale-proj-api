package dev.guarmo.whales.teleg;

import dev.guarmo.whales.model.transaction.deposit.dto.PostDepositDto;
import dev.guarmo.whales.model.transaction.invoice.dto.GetInvoiceDto;
import dev.guarmo.whales.model.user.UserCredentials;
import dev.guarmo.whales.model.transaction.withdraw.dto.GetWithdrawDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public void sendNotificationAboutSuccessTransaction(PostDepositDto dto) {
        String text = dto.toString();
        bot.prepareAndSendMessage(DELIVERY_CHAT_ID, text);
    }

    public void sendTextNotification(String formated) {
        String[] strings = splitStringForTg(formated);
        for (String string : strings) {
            bot.prepareAndSendMessage(DELIVERY_CHAT_ID, string);
        }
    }

    public static String[] splitStringForTg(String input) {
        int maxLength = 3900;
        if (input == null || input.isEmpty()) {
            return new String[]{""};
        }

        List<String> result = new ArrayList<>();
        int start = 0;
        while (start < input.length()) {
            int end = Math.min(input.length(), start + maxLength);

            // Ensure we don't split words
            if (end < input.length() && Character.isLetterOrDigit(input.charAt(end))) {
                int lastSpace = input.lastIndexOf(' ', end);
                if (lastSpace > start) {
                    end = lastSpace;
                }
            }

            result.add(input.substring(start, end).trim());
            start = end;
        }

        return result.toArray(new String[0]);
    }
}
