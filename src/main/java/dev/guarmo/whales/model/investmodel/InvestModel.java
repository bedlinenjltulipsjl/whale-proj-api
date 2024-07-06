package dev.guarmo.whales.model.investmodel;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString(exclude = {"details"})
@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class InvestModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer receivedBonusAtCycle;
    private InvestModelStatus investModelStatus;
    private Boolean isReceivedBonus;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private InvestModelsDetails details;
}
