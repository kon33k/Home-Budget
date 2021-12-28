package com.kon.budget.exception;

import com.kon.budget.enums.AuthenticationMessageEnum;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super(AuthenticationMessageEnum.USER_NOT_FOUND.getMessage());
    }
}
