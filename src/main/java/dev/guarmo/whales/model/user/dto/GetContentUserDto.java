package dev.guarmo.whales.model.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetContentUserDto {
    private Long id;
    private String login;
    private String name;
    private String username;
    private Double balanceAmount;
    private List<Long> depositIds;
    private List<Long> withdrawIds;
    private List<Long> incomeIds;
    private List<Long> purchaseIds;
}
