package com.kon.budget.controller.handler;

import com.kon.budget.controller.handler.dtos.ErrorMessage;
import com.kon.budget.exception.AssetIncompleteException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AssetControllerExceptionHandler {


    //zastepuje domyslny error po wyslaniu zlego jsona assert
    @ExceptionHandler
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorMessage assetIncompleteExceptionHandler(AssetIncompleteException exception) {
        return ErrorMessage.ErrorMessageBuilder.anErrorMessage()
                .withErrorCode(exception.getErrorCode())
                .withErrorDescription(exception.getMessage())
                .build();
    }
}
