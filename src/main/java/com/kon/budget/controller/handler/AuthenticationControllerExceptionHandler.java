package com.kon.budget.controller.handler;

import com.kon.budget.controller.handler.dtos.ErrorMessage;
import com.kon.budget.exception.InvalidUsernameOrPasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthenticationControllerExceptionHandler {

    /*
    wysyła komunikat o błednym logownaiu zmienia http status z 500 na 403
     */

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorMessage usernameOrPasswordIncorrectExceptionHandler(InvalidUsernameOrPasswordException exception) {
        return ErrorMessage.ErrorMessageBuilder.anErrorMessage()
                .withErrorDescription(exception.getMessage())
                .withErrorCode(String.valueOf(HttpStatus.FORBIDDEN.value()))
                .build();
    }
}
