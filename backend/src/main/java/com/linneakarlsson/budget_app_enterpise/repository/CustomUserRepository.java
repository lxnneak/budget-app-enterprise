package com.linneakarlsson.budget_app_enterpise.repository;

import com.linneakarlsson.budget_app_enterpise.model.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomUserRepository extends JpaRepository<CustomUser, Long> {

    Optional<CustomUser> findUserByEmail(String email);

    boolean existsByEmail(String email);
}

