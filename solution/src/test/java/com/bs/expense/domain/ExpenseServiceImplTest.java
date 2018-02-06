package com.bs.expense.domain;

import com.bs.expense.domain.model.Expense;
import com.bs.expense.infrastructure.ExpenseAssembler;
import com.bs.expense.infrastructure.driven.ExpenseEntity;
import com.bs.expense.infrastructure.driven.ExpenseRepository;
import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExpenseServiceImplTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private ExpenseAssembler assembler;

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    @Captor
    private ArgumentCaptor<ExpenseEntity> argumentCaptor;

    @Test
    public void shouldSaveAnExpense() throws Exception {
        Expense expense = new Expense(1L, BigDecimal.ONE, LocalDate.now(), "reason");
        ExpenseEntity entity = mock(ExpenseEntity.class);
        Expense expected = mock(Expense.class);
        when(assembler.toEntity(expense)).thenReturn(entity);
        when(assembler.toDomain(entity)).thenReturn(expected);
        when(expenseRepository.save(entity)).thenReturn(entity);

        Expense result = expenseService.save(expense);

        verify(expenseRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue(), is(entity));
        assertThat(result, Matchers.equalTo(expected));
    }

    @Test(expected = Exception.class)
    public void saveShouldThrowExceptionWhenTheInputIsNull() throws Exception {
        expenseService.save(null);
    }

    @Test
    public void shouldFindExpenseById() throws Exception {
        ExpenseEntity entity = mock(ExpenseEntity.class);
        Expense expected = mock(Expense.class);
        when(expenseRepository.findOne(1L)).thenReturn(entity);
        when(assembler.toDomain(entity)).thenReturn(expected);

        Optional<Expense> result = expenseService.findById(1L);

        assertThat(result, Matchers.equalTo(Optional.of(expected)));
    }

    @Test(expected = Exception.class)
    public void findByIdShouldThrowExceptionWhenTheInputIsNull() throws Exception {
        expenseService.findById(null);
    }

    @Test
    public void shouldReturnAllOfTheExpenses() throws Exception {
        ExpenseEntity entity = mock(ExpenseEntity.class);
        Expense expense = mock(Expense.class);
        when(expenseRepository.findAll()).thenReturn(Lists.newArrayList(entity));
        when(assembler.toDomain(entity)).thenReturn(expense);

        List<Expense> result = expenseService.findAll();

        assertThat(result, Matchers.containsInAnyOrder(expense));
    }
}