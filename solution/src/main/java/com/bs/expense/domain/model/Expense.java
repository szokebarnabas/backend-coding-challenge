package com.bs.expense.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Expense {
    private final Long id;
    private final BigDecimal amount;
    private final LocalDate date;
    private final String reason;
}
