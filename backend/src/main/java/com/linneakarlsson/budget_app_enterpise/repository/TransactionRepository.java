package com.linneakarlsson.budget_app_enterpise.repository;

import com.linneakarlsson.budget_app_enterpise.model.customUser.CustomUser;
import com.linneakarlsson.budget_app_enterpise.model.transaction.Transaction;
import com.linneakarlsson.budget_app_enterpise.model.transaction.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findByUserOrderByDateDesc(CustomUser user);

    List<Transaction> findByUserAndType(CustomUser user, TransactionType type);

    List<Transaction> findByUserAndDateBetween(CustomUser user, LocalDate from, LocalDate to);

    List<Transaction> findByUserAndCategory(CustomUser user, String category);
}

