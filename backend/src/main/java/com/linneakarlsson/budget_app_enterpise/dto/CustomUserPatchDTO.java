package com.linneakarlsson.budget_app_enterpise.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomUserPatchDTO(
        @Size(min = 5, max = 50)
        @Email
        String email,

        @Size(min = 8, max = 20)
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z0-9!@#$%^&*()_+=-]+$",
                message = "Must contain a capital letter and a number. May only contain A-Z, 0-9, !@#$%^&*()_+=-")
        String password
) {}

