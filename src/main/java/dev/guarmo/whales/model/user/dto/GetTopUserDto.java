package dev.guarmo.whales.model.user.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
public class GetTopUserDto {
    private Long id;
    private String name;
    private String username;
    private Double investedAmount;
    private Double earnedAmount;
    private LocalDateTime createdAt;
}
