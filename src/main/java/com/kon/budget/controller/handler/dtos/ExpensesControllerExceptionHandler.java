package com.kon.budget.controller.handler.dtos;

import com.kon.budget.exception.MissingExpensesFilterException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExpensesControllerExceptionHandler {

    /**
     * przechwytuje Exceptiony rzuczane podczas sprawdzania
     * filtrów i opakoduje je w error mesage
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
    public ErrorMessage missingExpensesFilterExceptionHandler(MissingExpensesFilterException exception) {
        return ErrorMessage.ErrorMessageBuilder.anErrorMessage()
                .withErrorCode(exception.getErrorCode())
                .withErrorDescription(exception.getMessage())
                .build();
    }
}
