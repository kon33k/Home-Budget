package com.kon.budget.controller.handler;

import com.kon.budget.controller.handler.dtos.ErrorMessage;
import com.kon.budget.exception.AssetIncompleteException;
import com.kon.budget.exception.MissingAssetsFilterException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AssetControllerExceptionHandler {

    /**
    * buduje error message i wyświetla go gdy pola dla assets w json sa niepoprawinie wpisane,
    * zatepuje domyslne wywoalnie spinga
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorMessage assetIncompleteExceptionHandler(AssetIncompleteException exception) {
        return ErrorMessage.ErrorMessageBuilder.anErrorMessage()
                .withErrorCode(exception.getErrorCode())
                .withErrorDescription(exception.getMessage())
                .build();
    }

    /**
     * przechwytuje Exceptiony rzuczane podczas sprawdzania
     * filtrów i opakoduje je w error mesage
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
    public ErrorMessage missingAssetsFilterException(MissingAssetsFilterException exception) {
        return ErrorMessage.ErrorMessageBuilder.anErrorMessage()
                .withErrorCode(exception.getErrorCode())
                .withErrorDescription(exception.getMessage())
                .build();
    }
}
