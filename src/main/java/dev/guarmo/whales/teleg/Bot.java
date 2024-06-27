package dev.guarmo.whales.teleg;

import dev.guarmo.whales.model.transaction.withdraw.WithdrawStatus;
import dev.guarmo.whales.service.WithdrawServiceHelper;
import dev.guarmo.whales.teleg.config.BotConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot {
    private final BotConfig config;
    static final String ERROR_TEXT = "Error occurred: ";
    private final WithdrawServiceHelper withdrawServiceHelper;

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            prepareAndSendMessage(chatId, messageText);
        }

        if (update.hasCallbackQuery()) {
            // Set variables
            CallbackQuery callback = update.getCallbackQuery();
            String callData = callback.getData();
            String getPrevMsgText = callback.getMessage().getText();
            Integer messageId = callback.getMessage().getMessageId();
            long chatId = callback.getMessage().getChatId();
            String data = callData.split("\\?")[0];
            String withdrawId = callData.split("\\?")[1];

            switch (data) {
                case "approve" -> {
                    EditMessageText new_message = new EditMessageText();
                    new_message.setChatId(String.valueOf(chatId));
                    new_message.setMessageId(messageId);
                    new_message.setText(getPrevMsgText + callData);
                    withdrawServiceHelper.updateWithdrawStatus(withdrawId, WithdrawStatus.APPROVED);
                    log.info("Setting withdrawId {} to status {}", withdrawId, WithdrawStatus.APPROVED);
                    try {
                        execute(new_message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException("Some kind of error here 1", e);
                    }
                }
                case "review" -> {
                    EditMessageText new_message = new EditMessageText();
                    new_message.setChatId(String.valueOf(chatId));
                    new_message.setMessageId(messageId);
                    new_message.setText(getPrevMsgText + callData);

                    List<List<InlineKeyboardButton>> rowsInline = getRowsForKeyboardReviewing(withdrawId);
                    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                    markupInline.setKeyboard(rowsInline);
                    new_message.setReplyMarkup(markupInline);

                    withdrawServiceHelper.updateWithdrawStatus(withdrawId, WithdrawStatus.REVIEWING);
                    log.info("Setting withdrawId {} to status {}", withdrawId, WithdrawStatus.REVIEWING);
                    try {
                        execute(new_message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException("Some kind of error here 2", e);
                    }
                }
                case "reject" -> {
                    EditMessageText new_message = new EditMessageText();
                    new_message.setChatId(String.valueOf(chatId));
                    new_message.setMessageId(messageId);
                    new_message.setText(getPrevMsgText + callData);
                    withdrawServiceHelper.updateWithdrawStatus(withdrawId, WithdrawStatus.REJECTED);
                    log.info("Setting withdrawId {} to status {}", withdrawId, WithdrawStatus.REJECTED);
                    try {
                        execute(new_message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException("Some kind of error here 2", e);
                    }
                }
            }
        }
    }

        public void sendMessageWithKeyboard(String text, long chatId, String login) {
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText(text);

            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInline = getRowsForKeyboard(login);
            // Add it to the message
            markupInline.setKeyboard(rowsInline);
            message.setReplyMarkup(markupInline);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                throw new RuntimeException("Error when sending msg", e);
            }
        }

        private static List<List<InlineKeyboardButton>> getRowsForKeyboard(String param) {
            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
            inlineKeyboardButton1.setText("Approve");
            inlineKeyboardButton1.setCallbackData("approve?" + param);
            rowInline.add(inlineKeyboardButton1);

            List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
            InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
            inlineKeyboardButton2.setText("Review");
            inlineKeyboardButton2.setCallbackData("review?" + param);
            rowInline2.add(inlineKeyboardButton2);

            List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
            InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
            inlineKeyboardButton3.setText("Reject");
            inlineKeyboardButton3.setCallbackData("reject?" + param);
            rowInline3.add(inlineKeyboardButton3);
            // Set the keyboard to the markup
            rowsInline.add(rowInline);
            rowsInline.add(rowInline2);
            rowsInline.add(rowInline3);
            return rowsInline;
        }

        private static List<List<InlineKeyboardButton>> getRowsForKeyboardReviewing(String param) {
            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
            inlineKeyboardButton1.setText("Approve");
            inlineKeyboardButton1.setCallbackData("approve?" + param);
            rowInline.add(inlineKeyboardButton1);

            List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
            InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
            inlineKeyboardButton3.setText("Reject");
            inlineKeyboardButton3.setCallbackData("reject?" + param);
            rowInline3.add(inlineKeyboardButton3);
            // Set the keyboard to the markup
            rowsInline.add(rowInline);
            rowsInline.add(rowInline3);
            return rowsInline;
        }

        private void executeMessage(SendMessage message){
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error(ERROR_TEXT + e.getMessage());
            }
        }

        public void prepareAndSendMessage(long chatId, String textToSend){
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText(textToSend);
            executeMessage(message);
        }
}
