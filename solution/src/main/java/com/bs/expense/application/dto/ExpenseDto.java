package com.bs.expense.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "amount",
        "vat",
        "date",
        "reason"
})
@Data
@Builder
public final class ExpenseDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("vat")
    private BigDecimal vat;

    @JsonProperty("date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;

    @JsonProperty("reason")
    private String reason;
}
