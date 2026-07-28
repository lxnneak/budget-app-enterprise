package com.linneakarlsson.budget_app_enterpise.repository;

import com.linneakarlsson.budget_app_enterpise.model.customUser.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomUserRepository extends JpaRepository<CustomUser, UUID> {

    Optional<CustomUser> findUserByEmail(String email);

    boolean existsByEmail(String email);
}

