package dev.guarmo.whales.model.investmodel;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class InvestModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer cyclesCount;
    private InvestModelStatus investModelStatus;

    @Column(updatable = false)
    private LocalDateTime unlockDate;
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    private InvestModelsDetails details;
}
