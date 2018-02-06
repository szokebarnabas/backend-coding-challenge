package com.bs.expense.infrastructure.driver;

import com.bs.expense.application.dto.ErrorDto;
import com.bs.expense.application.dto.ExpenseDto;
import com.bs.expense.domain.ExpenseService;
import com.bs.expense.domain.model.Expense;
import com.bs.expense.infrastructure.ExpenseAssembler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/expense")
@Slf4j
public class ExpenseController {

    private final ExpenseService expenseService;
    private final ExpenseAssembler assembler;

    @Autowired
    public ExpenseController(ExpenseService expenseService, ExpenseAssembler assembler) {
        this.expenseService = expenseService;
        this.assembler = assembler;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ExpenseDto> persist(@RequestBody final ExpenseDto request) {
        final Expense domain = assembler.toDomain(request);
        return ResponseEntity.ok(assembler.toDto(expenseService.save(domain)));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ExpenseDto> findById(@PathVariable final Long id) {
        return expenseService.findById(id)
                .map(assembler::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ExpenseDto>> findById() {
        List<ExpenseDto> result = expenseService.findAll()
                .stream()
                .map(assembler::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }


    @ExceptionHandler({IllegalArgumentException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorDto> badRequestHandler(final Exception ex) {
        log.error("Bad request.", ex);
        ErrorDto error = new ErrorDto(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorDto> genericErrHandler(final Exception ex) {
        log.error("Failed to create response.", ex);
        final ErrorDto error = new ErrorDto(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
