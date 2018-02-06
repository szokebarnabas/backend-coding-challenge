package com.bs.expense.infrastructure.driven;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends CrudRepository<ExpenseEntity, Long> {
}
