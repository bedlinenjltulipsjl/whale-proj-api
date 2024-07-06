package dev.guarmo.whales.model.user;

import dev.guarmo.whales.model.investmodel.InvestModel;
import dev.guarmo.whales.model.transaction.income.Income;
import dev.guarmo.whales.model.transaction.purchase.Purchase;
import dev.guarmo.whales.model.transaction.deposit.Deposit;
import dev.guarmo.whales.model.transaction.withdraw.MoneyWithdraw;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SoftDelete;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Setter
@Getter
@ToString(exclude = {
        "password",
        "bottomReferrals",
        "upperReferral",
        "withdraws",
        "deposits",
        "incomes",
        "purchases",
        "reflink",
})
@Entity
@SoftDelete
@NoArgsConstructor
public class UserCredentials implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String login;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private RoleStatus role;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @NonNull
    private String name;
    private String username;
    private double balanceAmount;
    @OneToMany
    private List<Deposit> deposits;
    @OneToMany
    private List<MoneyWithdraw> withdraws;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private List<Income> incomes;
    @OneToMany
    private List<Purchase> purchases;
    private String reflink;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private List<InvestModel> investModels;
    @OneToMany
    private List<UserCredentials> bottomReferrals;
    @ManyToOne
    private UserCredentials upperReferral;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
