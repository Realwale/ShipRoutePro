package com.shiproutepro.backend.repository.user;

import com.shiproutepro.backend.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Long, AppUser> {
}
