package com.bs.expense.domain;

import com.bs.expense.domain.model.Expense;
import com.bs.expense.infrastructure.ExpenseAssembler;
import com.bs.expense.infrastructure.driven.ExpenseEntity;
import com.bs.expense.infrastructure.driven.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseAssembler assembler;

    @Autowired
    public ExpenseServiceImpl(final ExpenseRepository expenseRepository, final ExpenseAssembler assembler) {
        this.expenseRepository = expenseRepository;
        this.assembler = assembler;
    }

    @Override
    public List<Expense> findAll() {
        Iterable<ExpenseEntity> iterable = () -> expenseRepository.findAll().iterator();
        return StreamSupport
                .stream(iterable.spliterator(), false)
                .map(assembler::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Expense save(final Expense expense) {
        requireNonNull(expense, "The expense cannot be null.");
        final ExpenseEntity entity = expenseRepository.save(assembler.toEntity(expense));
        return assembler.toDomain(entity);
    }

    @Override
    public Optional<Expense> findById(final Long expenseId) {
        requireNonNull(expenseId, "The expenseId cannot be null.");
        final ExpenseEntity entity = expenseRepository.findOne(expenseId);
        return ofNullable(entity).map(assembler::toDomain);
    }
}
