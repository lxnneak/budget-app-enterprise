package com.linneakarlsson.budget_app_enterpise.controller;

import com.linneakarlsson.budget_app_enterpise.dto.TransactionRequestDTO;
import com.linneakarlsson.budget_app_enterpise.dto.TransactionResponseDTO;
import com.linneakarlsson.budget_app_enterpise.model.customUser.CustomUserDetails;
import com.linneakarlsson.budget_app_enterpise.model.transaction.TransactionType;
import com.linneakarlsson.budget_app_enterpise.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    private UUID getUserId(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getCustomUser().getId();
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getAll(Authentication authentication) {
        List<TransactionResponseDTO> transactions = service.getAll(getUserId(authentication));
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getById(
            @PathVariable UUID id,
            Authentication authentication) {
        TransactionResponseDTO transaction = service.getById(id, getUserId(authentication));
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<TransactionResponseDTO>> getByType(
            @PathVariable TransactionType type,
            Authentication authentication) {
        List<TransactionResponseDTO> transactions = service.getByType(type, getUserId(authentication));
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/range")
    public ResponseEntity<List<TransactionResponseDTO>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Authentication authentication) {
        List<TransactionResponseDTO> transactions = service.getByDateRange(from, to, getUserId(authentication));
        return ResponseEntity.ok(transactions);
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> create(
            @Valid @RequestBody TransactionRequestDTO request,
            Authentication authentication) {
        TransactionResponseDTO createdTransaction = service.create(request, getUserId(authentication));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody TransactionRequestDTO request,
            Authentication authentication) {
        TransactionResponseDTO updatedTransaction = service.update(id, request, getUserId(authentication));
        return ResponseEntity.ok(updatedTransaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id,
            Authentication authentication) {
        service.delete(id, getUserId(authentication));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getBalance(
            Authentication authentication) {
        BigDecimal balance = service.getBalance(getUserId(authentication));
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<TransactionResponseDTO>> getByCategory(
            @PathVariable String category,
            Authentication authentication) {
        List<TransactionResponseDTO> transactions = service.getByCategory(category, getUserId(authentication));
        return ResponseEntity.ok(transactions);
    }
}