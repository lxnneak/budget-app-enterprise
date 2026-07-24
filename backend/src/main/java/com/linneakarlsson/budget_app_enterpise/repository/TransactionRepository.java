package com.linneakarlsson.budget_app_enterpise.repository;

import com.linneakarlsson.budget_app_enterpise.model.CustomUser;
import com.linneakarlsson.budget_app_enterpise.model.Transaction;
import com.linneakarlsson.budget_app_enterpise.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserOrderByDateDesc(CustomUser user);

    List<Transaction> findByUserAndType(CustomUser user, TransactionType type);

    List<Transaction> findByUserAndDateBetween(CustomUser user, LocalDate from, LocalDate to);

    List<Transaction> findByUserAndCategory(CustomUser user, String category);
}

