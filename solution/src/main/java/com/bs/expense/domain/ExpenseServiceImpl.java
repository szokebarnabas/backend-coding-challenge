package com.bs.expense.domain;

import com.bs.expense.domain.model.Expense;
import com.bs.expense.infrastructure.ExpenseAssembler;
import com.bs.expense.infrastructure.driven.ExpenseEntity;
import com.bs.expense.infrastructure.driven.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseAssembler assembler;
    private final BigDecimal vatPercent;

    @Autowired
    public ExpenseServiceImpl(final ExpenseRepository expenseRepository,
                              final ExpenseAssembler assembler,
                              @Value("${expense-api.vat-percent}") final BigDecimal vatPercent) {
        this.expenseRepository = expenseRepository;
        this.assembler = assembler;
        this.vatPercent = vatPercent;
    }

    @Override
    public List<Expense> findAll() {
        final Iterable<ExpenseEntity> iterable = () -> expenseRepository.findAll().iterator();
        return StreamSupport
                .stream(iterable.spliterator(), false)
                .map(assembler::toDomain)
                .collect(Collectors.toList());
    }

    private Expense calculateVat(Expense expense) {
        final BigDecimal vat = expense
                .getAmount()
                .multiply(vatPercent.divide(BigDecimal.valueOf(100)));

        return new Expense(expense.getId(), vat, expense.getAmount(), expense.getDate(), expense.getReason());
    }

    @Override
    public Expense save(final Expense expense) {
        requireNonNull(expense, "The expense cannot be null.");
        if (expense.getAmount().doubleValue() <= 0) {
            throw new IllegalArgumentException("The amount has to be greater than 0");
        }
        Expense amendedWithVat = calculateVat(expense);
        final ExpenseEntity entity = expenseRepository.save(assembler.toEntity(amendedWithVat));
        return assembler.toDomain(entity);
    }

    @Override
    public Optional<Expense> findById(final Long expenseId) {
        requireNonNull(expenseId, "The expenseId cannot be null.");
        final ExpenseEntity entity = expenseRepository.findOne(expenseId);
        return ofNullable(entity).map(assembler::toDomain);
    }
}
