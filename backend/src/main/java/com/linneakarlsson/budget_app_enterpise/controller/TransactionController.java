package com.linneakarlsson.budget_app_enterpise.controller;

import com.linneakarlsson.budget_app_enterpise.dto.TransactionRequestDTO;
import com.linneakarlsson.budget_app_enterpise.dto.TransactionResponseDTO;
import com.linneakarlsson.budget_app_enterpise.model.customUser.CustomUser;
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

    @GetMapping
    public List<TransactionResponseDTO> getAll(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return service.getAll(userDetails.getCustomUser().getId());
    }

    @GetMapping("/{id}")
    public TransactionResponseDTO getById(
            @PathVariable UUID id,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        CustomUser user = userDetails.getCustomUser();
        return service.getById(id, user);
    }

    @GetMapping("/type/{type}")
    public List<TransactionResponseDTO> getByType(
            @PathVariable TransactionType type,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        CustomUser user = userDetails.getCustomUser();
        return service.getByType(type, user);
    }

    @GetMapping("/range")
    public List<TransactionResponseDTO> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        CustomUser user = userDetails.getCustomUser();
        return service.getByDateRange(from, to, user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponseDTO create(
            @Valid @RequestBody TransactionRequestDTO request,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        CustomUser user = userDetails.getCustomUser();
        return service.create(request, user);
    }

    @PutMapping("/{id}")
    public TransactionResponseDTO update(
            @PathVariable UUID id,
            @Valid @RequestBody TransactionRequestDTO request,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        CustomUser user = userDetails.getCustomUser();
        return service.update(id, request, user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable UUID id,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        CustomUser user = userDetails.getCustomUser();
        service.delete(id, user);
    }

    @GetMapping("/balance")
    public BigDecimal getBalance(
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        CustomUser user = userDetails.getCustomUser();
        return service.getBalance(user);
    }

    @GetMapping("/category/{category}")
    public List<TransactionResponseDTO> getByCategory(
            @PathVariable String category,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        CustomUser user = userDetails.getCustomUser();
        return service.getByCategory(category, user);
    }
}
