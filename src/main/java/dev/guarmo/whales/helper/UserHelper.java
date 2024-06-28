package dev.guarmo.whales.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class UserHelper {
    @Value("${bot.link}")
    private String botLink;

    public String generateRefLinkForUser(String userTelegramId) {
        return botLink + Base64.getUrlEncoder().encodeToString(userTelegramId.getBytes(StandardCharsets.UTF_8));
    }
}
