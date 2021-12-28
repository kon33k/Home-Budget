package com.kon.budget.exception;

import com.kon.budget.enums.AuthenticationMessageEnum;

public class UserAlreadyExistException extends RuntimeException {

    public UserAlreadyExistException() {
        super(AuthenticationMessageEnum.USER_ALREADY_EXIST.getMessage());
    }
}
