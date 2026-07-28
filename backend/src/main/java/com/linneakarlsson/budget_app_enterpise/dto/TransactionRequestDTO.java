package com.linneakarlsson.budget_app_enterpise.dto;

import com.linneakarlsson.budget_app_enterpise.model.transaction.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequestDTO(
        @NotNull(message = "Type is required (INCOME or EXPENSE)") TransactionType type,
        @NotNull(message = "Amount is required") @Positive(message = "Amount must be positive") BigDecimal amount,
        String category,
        String description,
        @NotNull(message = "Date is required") LocalDate date
) {
}

