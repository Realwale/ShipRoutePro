package com.shiproutepro.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "jwt_token")
public class JwtToken extends BaseEntity{

    @Column(name = "token", unique = true)
    private String token;
    @Column(unique = true)
    private String refreshToken;
    private boolean isExpired;
    private boolean isRevoked;

    @Column(name = "expiry_date")
    private Date expiryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private AppUser user;

}