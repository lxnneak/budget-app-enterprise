package com.linneakarlsson.budget_app_enterpise.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CustomUserLoginDTO(
        @NotBlank(message = "Email cannot be empty")
        @Email
        String email,

        @NotBlank(message = "Password must not be empty")
        String password
) {
}
