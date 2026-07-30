package com.linneakarlsson.budget_app_enterpise.service;

import com.linneakarlsson.budget_app_enterpise.model.customUser.CustomUser;
import com.linneakarlsson.budget_app_enterpise.repository.CustomUserRepository;
import com.linneakarlsson.budget_app_enterpise.config.AppPasswordConfig;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthenticationService {

    private final CustomUserRepository customUserRepository;

    public AuthenticationService(CustomUserRepository customUserRepository) {
        this.customUserRepository = customUserRepository;
    }

    public CustomUser authenticateOrThrow(String email, String rawPassword) {
        return customUserRepository.findUserByEmail(email)
                .filter(user -> AppPasswordConfig.verifyPassword(rawPassword, user.getPassword()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
    }
}
