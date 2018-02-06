package com.bs.expense.infrastructure;

import com.bs.expense.application.dto.ExpenseDto;
import com.bs.expense.domain.model.Expense;
import com.bs.expense.infrastructure.driven.ExpenseEntity;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertThat;

public class ExpenseAssemblerTest {

    private static final LocalDate DATE = LocalDate.of(2018, 02, 05);
    private final ExpenseAssembler expenseAssembler = new ExpenseAssembler();

    @Test
    public void shouldCreateDomainFromDto() {
        ExpenseDto dto = ExpenseDto.builder()
                .id(34L)
                .amount("334.532")
                .vat(BigDecimal.TEN)
                .reason("reason")
                .date(DATE)
                .build();
        Expense domain = new Expense(34L, BigDecimal.valueOf(10), BigDecimal.valueOf(334.532), DATE, "reason");

        Expense result = expenseAssembler.toDomain(dto);

        assertThat(result, Matchers.equalTo(domain));
    }

    @Test
    public void shouldCreateDtoFromDomain() {
        ExpenseDto dto = ExpenseDto.builder()
                .id(34L)
                .vat(BigDecimal.TEN)
                .amount("334.532")
                .reason("reason")
                .date(DATE)
                .build();
        Expense domain = new Expense(34L, BigDecimal.valueOf(10), BigDecimal.valueOf(334.532), DATE, "reason");

        ExpenseDto result = expenseAssembler.toDto(domain);

        assertThat(result, Matchers.equalTo(dto));
    }

    @Test
    public void shouldCreateEntityFromDomain() {
        Expense domain = new Expense(34L, BigDecimal.valueOf(10), BigDecimal.valueOf(334.532), DATE, "reason");
        ExpenseEntity entity = new ExpenseEntity(34L, BigDecimal.valueOf(334.532), BigDecimal.valueOf(10), DATE, "reason");

        ExpenseEntity result = expenseAssembler.toEntity(domain);

        assertThat(result, Matchers.equalTo(entity));
    }

    @Test
    public void shouldCreateDomainFromEntity() {
        ExpenseEntity entity = new ExpenseEntity(34L, BigDecimal.valueOf(10), BigDecimal.valueOf(334.532), DATE, "reason");
        Expense domain = new Expense(34L, BigDecimal.valueOf(334.532), BigDecimal.valueOf(10), DATE, "reason");

        Expense result = expenseAssembler.toDomain(entity);

        assertThat(result, Matchers.equalTo(domain));
    }
}