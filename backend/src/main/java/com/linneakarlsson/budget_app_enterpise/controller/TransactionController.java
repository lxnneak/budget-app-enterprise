package com.linneakarlsson.budget_app_enterpise.controller;

import com.linneakarlsson.budget_app_enterpise.dto.TransactionRequestDTO;
import com.linneakarlsson.budget_app_enterpise.dto.TransactionResponseDTO;
import com.linneakarlsson.budget_app_enterpise.model.customUser.CustomUser;
import com.linneakarlsson.budget_app_enterpise.model.transaction.TransactionType;
import com.linneakarlsson.budget_app_enterpise.service.AuthenticationService;
import com.linneakarlsson.budget_app_enterpise.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService service;
    private final AuthenticationService authenticationService;

    public TransactionController(TransactionService service, AuthenticationService authenticationService) {
        this.service = service;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public List<TransactionResponseDTO> getAll(
            @RequestHeader String email,
            @RequestHeader String password) {
        CustomUser user = authenticationService.authenticateOrThrow(email, password);
        return service.getAll(user);
    }

    @GetMapping("/{id}")
    public TransactionResponseDTO getById(
            @PathVariable UUID id,
            @RequestHeader String email,
            @RequestHeader String password) {
        CustomUser user = authenticationService.authenticateOrThrow(email, password);
        return service.getById(id, user);
    }

    @GetMapping("/type/{type}")
    public List<TransactionResponseDTO> getByType(
            @PathVariable TransactionType type,
            @RequestHeader String email,
            @RequestHeader String password) {
        CustomUser user = authenticationService.authenticateOrThrow(email, password);
        return service.getByType(type, user);
    }

    @GetMapping("/range")
    public List<TransactionResponseDTO> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestHeader String email,
            @RequestHeader String password) {
        CustomUser user = authenticationService.authenticateOrThrow(email, password);
        return service.getByDateRange(from, to, user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponseDTO create(
            @Valid @RequestBody TransactionRequestDTO request,
            @RequestHeader String email,
            @RequestHeader String password) {
        CustomUser user = authenticationService.authenticateOrThrow(email, password);
        return service.create(request, user);
    }

    @PutMapping("/{id}")
    public TransactionResponseDTO update(
            @PathVariable UUID id,
            @Valid @RequestBody TransactionRequestDTO request,
            @RequestHeader String email,
            @RequestHeader String password) {
        CustomUser user = authenticationService.authenticateOrThrow(email, password);
        return service.update(id, request, user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable UUID id,
            @RequestHeader String email,
            @RequestHeader String password) {
        CustomUser user = authenticationService.authenticateOrThrow(email, password);
        service.delete(id, user);
    }

    @GetMapping("/balance")
    public BigDecimal getBalance(
            @RequestHeader String email,
            @RequestHeader String password) {
        CustomUser user = authenticationService.authenticateOrThrow(email, password);
        return service.getBalance(user);
    }

    @GetMapping("/category/{category}")
    public List<TransactionResponseDTO> getByCategory(
            @PathVariable String category,
            @RequestHeader String email,
            @RequestHeader String password) {
        CustomUser user = authenticationService.authenticateOrThrow(email, password);
        return service.getByCategory(category, user);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNotFound(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
