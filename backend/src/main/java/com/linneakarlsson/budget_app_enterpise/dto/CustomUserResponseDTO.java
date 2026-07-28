package com.linneakarlsson.budget_app_enterpise.dto;

import com.linneakarlsson.budget_app_enterpise.model.customUser.authority.UserRole;

import java.util.Set;
import java.util.UUID;

public record CustomUserResponseDTO(
        UUID id,
        String email,
        Set<UserRole> role
) {}

