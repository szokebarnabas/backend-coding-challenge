package com.bs.expense.infrastructure;

import com.bs.expense.application.dto.ExpenseDto;
import com.bs.expense.domain.model.Expense;
import com.bs.expense.infrastructure.driven.ExpenseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static java.util.Objects.requireNonNull;

@Component
public class ExpenseAssembler {

    public Expense toDomain(final ExpenseDto request) {
        requireNonNull(request, "The dto cannot be null.");
        return new Expense(
                request.getId(),
                new BigDecimal(request.getAmount()),
                request.getDate(),
                request.getReason()
        );
    }

    public ExpenseDto toDto(final Expense expense) {
        requireNonNull(expense, "The expense cannot be null.");
        return ExpenseDto.builder()
                .amount(expense.getAmount().toString())
                .date(expense.getDate())
                .reason(expense.getReason())
                .id(expense.getId())
                .build();
    }

    public ExpenseEntity toEntity(Expense expense) {
        requireNonNull(expense, "The expense cannot be null.");
        return ExpenseEntity.builder()
                .amount(expense.getAmount())
                .date(expense.getDate())
                .reason(expense.getReason())
                .id(expense.getId())
                .build();
    }

    public Expense toDomain(ExpenseEntity entity) {
        requireNonNull(entity, "The entity cannot be null.");
        return new Expense(
                entity.getId(),
                entity.getAmount(),
                entity.getDate(),
                entity.getReason()
        );
    }
}
