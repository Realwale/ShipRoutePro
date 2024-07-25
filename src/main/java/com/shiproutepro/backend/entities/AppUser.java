package com.shiproutepro.backend.entities;

import com.shiproutepro.backend.enums.AccountCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
@Entity
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String companyName;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String password;
    private String companyAddress;
    private int failedLoginAttempts;
    private boolean isEmailVerified;
    @CreationTimestamp
    private LocalDateTime createdOn;
    private LocalDateTime lockTime;

    @Column(name = "account_type")
    @Enumerated(EnumType.STRING)
    private AccountCategory type;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JwtToken> jwtTokens;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(type.name()));
    }

    @Override
    public String getUsername() {
        return email;
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

    @Override
    public String getPassword() {
        return password;
    }

    public boolean isAccountLocked() {
        if (lockTime == null) {
            return false;
        }
        return lockTime.isAfter(LocalDateTime.now());
    }

    public void increaseFailedAttempts() {
        this.failedLoginAttempts++;
    }

    public void resetFailedAttempts() {
        this.failedLoginAttempts = 0;
    }

    public void lockAccount(int min) {
        this.lockTime = LocalDateTime.now().plusMinutes(min);
    }

    public void unlockAccount() {
        this.lockTime = null;
    }

}
