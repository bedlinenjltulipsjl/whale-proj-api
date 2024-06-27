package dev.guarmo.whales.model.user.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class GetUserWithReferralsDto {
    private String id;
    private String login;
    private String name;
    private String username;
    private Integer treeLevel;
    private List<GetUserWithReferralsDto> bottomReferral;
}
