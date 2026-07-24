package com.linneakarlsson.budget_app_enterpise.dto;

import com.linneakarlsson.budget_app_enterpise.model.Role;

public record CustomUserResponseDTO(
        Long id,
        String email,
        Role role
) {}

