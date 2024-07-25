package com.shiproutepro.backend.repository;


import com.shiproutepro.backend.entities.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseEntityJpaRepository<T extends BaseEntity> extends JpaRepository<T, Long> {
}
