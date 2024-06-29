package dev.guarmo.whales.model.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetContentWithoutHistoryUserDto {
    private Long id;
    private String login;
    private String name;
    private String username;
    private Double balanceAmount;
}
