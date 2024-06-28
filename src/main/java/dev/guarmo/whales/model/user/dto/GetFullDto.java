package dev.guarmo.whales.model.user.dto;

import dev.guarmo.whales.model.investmodel.InvestModel;
import dev.guarmo.whales.model.investmodel.dto.GetInvestModel;
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
    private String reflink;
    private Double balanceAmount;
    private List<GetInvestModel> investModels;
    private List<GetFullDto> bottomReferrals;
    private List<GetTransaction> transactions;
}
