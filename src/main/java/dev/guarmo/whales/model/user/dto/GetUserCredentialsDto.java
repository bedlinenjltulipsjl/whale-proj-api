package dev.guarmo.whales.model.user.dto;

import dev.guarmo.whales.model.user.RoleStatus;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class GetUserCredentialsDto {
    private String id;
    private String login;
    private String name;
    private String username;
    private String upperReferralLogin;
    private RoleStatus role;
    private LocalDateTime createdAt;
}
