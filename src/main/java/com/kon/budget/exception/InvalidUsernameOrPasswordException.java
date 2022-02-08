package com.kon.budget.exception;

import com.kon.budget.enums.AuthenticationMessageEnum;

public class InvalidUsernameOrPasswordException extends RuntimeException {

    /**
    * niepoprawnie wpisane credecaiale usera
     */

    public InvalidUsernameOrPasswordException() {
        super(AuthenticationMessageEnum.INVALID_USERNAME_OR_PASSWORD.getMessage());
    }
}
