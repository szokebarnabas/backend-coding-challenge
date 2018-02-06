package com.bs.expense.domain.model;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value(staticConstructor = "of")
@AllArgsConstructor
public class Expense {
    private final Long id;
    private final BigDecimal vat;
    private final BigDecimal amount;
    private final LocalDate date;
    private final String reason;
}
