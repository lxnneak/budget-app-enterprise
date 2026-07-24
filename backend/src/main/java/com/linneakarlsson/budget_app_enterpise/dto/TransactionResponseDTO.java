package com.linneakarlsson.budget_app_enterpise.dto;

import com.linneakarlsson.budget_app_enterpise.model.Transaction;
import com.linneakarlsson.budget_app_enterpise.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponseDTO(
        Long id,
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

