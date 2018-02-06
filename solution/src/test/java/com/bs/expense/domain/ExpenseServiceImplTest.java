package com.bs.expense.domain;

import com.bs.expense.domain.model.Expense;
import com.bs.expense.infrastructure.ExpenseAssembler;
import com.bs.expense.infrastructure.driven.ExpenseEntity;
import com.bs.expense.infrastructure.driven.ExpenseRepository;
import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.Before;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExpenseServiceImplTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private ExpenseAssembler assembler;

    @Captor
    private ArgumentCaptor<ExpenseEntity> argumentCaptor;

    private static final Expense EXPENSE = new Expense(1L,BigDecimal.valueOf(10), BigDecimal.ONE, LocalDate.now(), "reason");
    private ExpenseService expenseService;

    @Before
    public void setUp() throws Exception {
        expenseService = new ExpenseServiceImpl(expenseRepository, assembler, BigDecimal.valueOf(20));
    }

    @Test
    public void shouldSaveAnExpense() throws Exception {
        ExpenseEntity entity = mock(ExpenseEntity.class);
        when(assembler.toEntity(any(Expense.class))).thenReturn(entity);
        when(assembler.toDomain(entity)).thenReturn(EXPENSE);
        when(expenseRepository.save(org.mockito.Matchers.any(ExpenseEntity.class))).thenReturn(entity);

        Expense result = expenseService.save(EXPENSE);

        verify(expenseRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue(), is(entity));
        assertThat(result, Matchers.equalTo(EXPENSE));
    }

    @Test(expected = Exception.class)
    public void saveShouldThrowExceptionWhenTheInputIsNull() throws Exception {
        expenseService.save(null);
    }

    @Test
    public void shouldFindExpenseById() throws Exception {
        ExpenseEntity entity = mock(ExpenseEntity.class);
        when(expenseRepository.findOne(1L)).thenReturn(entity);
        when(assembler.toDomain(entity)).thenReturn(EXPENSE);

        Optional<Expense> result = expenseService.findById(1L);

        assertThat(result, Matchers.equalTo(Optional.of(EXPENSE)));
    }

    @Test(expected = Exception.class)
    public void findByIdShouldThrowExceptionWhenTheInputIsNull() throws Exception {
        expenseService.findById(null);
    }

    @Test
    public void shouldReturnAllOfTheExpenses() throws Exception {
        ExpenseEntity entity = mock(ExpenseEntity.class);
        when(expenseRepository.findAll()).thenReturn(Lists.newArrayList(entity));
        when(assembler.toDomain(entity)).thenReturn(EXPENSE);

        List<Expense> result = expenseService.findAll();

        assertThat(result, Matchers.containsInAnyOrder(EXPENSE));
    }
}