package com.linneakarlsson.budget_app_enterpise.service;

import com.linneakarlsson.budget_app_enterpise.dto.TransactionRequestDTO;
import com.linneakarlsson.budget_app_enterpise.dto.TransactionResponseDTO;
import com.linneakarlsson.budget_app_enterpise.model.customUser.CustomUser;
import com.linneakarlsson.budget_app_enterpise.model.transaction.Transaction;
import com.linneakarlsson.budget_app_enterpise.model.transaction.TransactionType;
import com.linneakarlsson.budget_app_enterpise.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public List<TransactionResponseDTO> getAll(UUID userId) {
        return repository.findByUserIdOrderByDateDesc(userId).stream().map(t -> TransactionResponseDTO.toDTO(t)).toList();
    }

    public TransactionResponseDTO getById(UUID id, CustomUser user) {
        Transaction transaction = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Transaction not found with id: " + id));
        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new NoSuchElementException("Transaction not found with id: " + id);
        }
        return TransactionResponseDTO.toDTO(transaction);
    }

    public List<TransactionResponseDTO> getByType(TransactionType type, CustomUser user) {
        return repository.findByUserAndType(user, type).stream().map(t -> TransactionResponseDTO.toDTO(t)).toList();
    }

    public List<TransactionResponseDTO> getByDateRange(LocalDate fromDate, LocalDate toDate, CustomUser user) {
        return repository.findByUserAndDateBetween(user, fromDate, toDate).stream().map(t -> TransactionResponseDTO.toDTO(t)).toList();
    }

    public TransactionResponseDTO create(TransactionRequestDTO request, CustomUser user) {
        Transaction transaction = new Transaction(
                request.type(),
                request.amount(),
                request.category(),
                request.description(),
                request.date(),
                user
        );
        return TransactionResponseDTO.toDTO(repository.save(transaction));
    }

    public TransactionResponseDTO update(UUID id, TransactionRequestDTO request, CustomUser user) {
        Transaction transaction = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Transaction not found with id: " + id));
        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new NoSuchElementException("Transaction not found with id: " + id);
        }
        transaction.setType(request.type());
        transaction.setAmount(request.amount());
        transaction.setCategory(request.category());
        transaction.setDescription(request.description());
        transaction.setDate(request.date());
        return TransactionResponseDTO.toDTO(repository.save(transaction));
    }

    public void delete(UUID id, CustomUser user) {
        Transaction transaction = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Transaction not found with id: " + id));
        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new NoSuchElementException("Transaction not found with id: " + id);
        }
        repository.deleteById(id);
    }

    public BigDecimal getBalance(CustomUser user) {
        BigDecimal totalIncome = repository.findByUserAndType(user, TransactionType.INCOME).stream()
                .map(t -> t.getAmount())
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

        BigDecimal totalExpense = repository.findByUserAndType(user, TransactionType.EXPENSE).stream()
                .map(t -> t.getAmount())
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

        return totalIncome.subtract(totalExpense);
    }

    public List<TransactionResponseDTO> getByCategory(String category, CustomUser user) {
        return repository.findByUserAndCategory(user, category).stream().map(t -> TransactionResponseDTO.toDTO(t)).toList();
    }
}
