package dev.guarmo.whales.model.user.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
public class GetTopUserDto {
    private String id;
    private String name;
    private String username;
    private String investedAmount;
    private String earnedAmount;
    private LocalDateTime createdAt;
}
