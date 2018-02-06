package com.bs.expense.infrastructure.driven;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Expense")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseEntity {

    @Id
    @Column(nullable = false, name = "id", unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, name = "amount")
    private BigDecimal amount;

    @Column(nullable = false, name = "date")
    private LocalDate date;

    @Column(nullable = false, name = "reason")
    private String reason;
}
