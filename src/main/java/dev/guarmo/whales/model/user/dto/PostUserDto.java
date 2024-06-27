package dev.guarmo.whales.model.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostUserDto {
    private String login;
    private String password;
    private String name;
    private String username;
    private String referralLogin;
}
