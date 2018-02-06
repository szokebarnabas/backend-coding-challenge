package com.bs.expense.domain;

import com.bs.expense.domain.model.Expense;

import java.util.List;
import java.util.Optional;

public interface ExpenseService {
    List<Expense> findAll();

    Expense save(final Expense expense);

    Optional<Expense> findById(final Long expenseId);
}
