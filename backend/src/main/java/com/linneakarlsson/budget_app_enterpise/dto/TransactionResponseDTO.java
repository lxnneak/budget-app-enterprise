package com.linneakarlsson.budget_app_enterpise.dto;

import com.linneakarlsson.budget_app_enterpise.model.transaction.Transaction;
import com.linneakarlsson.budget_app_enterpise.model.transaction.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TransactionResponseDTO(
        UUID id,
        TransactionType type,
        BigDecimal amount,
        String category,
        String description,
        LocalDate date
) {
    public static TransactionResponseDTO toDTO(Transaction transaction) {
        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getCategory(),
                transaction.getDescription(),
                transaction.getDate()
        );
    }
}

