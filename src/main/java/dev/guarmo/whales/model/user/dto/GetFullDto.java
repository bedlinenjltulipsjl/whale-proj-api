package dev.guarmo.whales.model.user.dto;

import dev.guarmo.whales.model.transaction.GetTransaction;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class GetFullDto {
    private String id;
    private String login;
    private String name;
    private String username;
    private Integer treeLevel;
    private List<GetFullDto> bottomReferrals;
    private List<GetTransaction> transactions;
}
