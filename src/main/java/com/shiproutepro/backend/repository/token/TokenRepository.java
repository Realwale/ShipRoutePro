package com.shiproutepro.backend.repository.token;

import com.shiproutepro.backend.entities.AppUser;
import com.shiproutepro.backend.entities.JwtToken;
import com.shiproutepro.backend.repository.BaseEntityJpaRepository;


import java.util.Optional;

public interface TokenRepository extends BaseEntityJpaRepository<JwtToken> {

    Optional<JwtToken> findByToken(String token);

    JwtToken findByUser(AppUser appUser);

    boolean existsByUser(AppUser appUser);

    JwtToken findByRefreshToken(String refreshToken);

    void deleteByUser(AppUser user);
}