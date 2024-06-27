package dev.guarmo.whales.model.user;

import dev.guarmo.whales.model.transaction.income.Income;
import dev.guarmo.whales.model.transaction.purchase.Purchase;
import dev.guarmo.whales.model.transaction.deposit.Deposit;
import dev.guarmo.whales.model.transaction.withdraw.MoneyWithdraw;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
public class UserCredentials implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(unique = true, nullable = false)
    private String login;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private RoleStatus role;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private String name;
    private String username;
    private double balanceAmount;
    @OneToMany
    private List<Deposit> deposits;
    @OneToMany
    private List<MoneyWithdraw> withdraws;
    @OneToMany
    private List<Income> incomes;
    @OneToMany
    private List<Purchase> purchases;
    @ManyToOne
    private UserCredentials upperReferral;
    @OneToMany
    private List<UserCredentials> bottomReferrals;

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
