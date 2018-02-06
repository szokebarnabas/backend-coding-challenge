package com.bs.expense.application.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public final class ErrorDto {
    private final String errorMsg;
}