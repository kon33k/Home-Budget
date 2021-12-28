package com.kon.budget.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AuthenticationMessageEnum {

    USER_NOT_FOUND("user not found"),
    USER_ALREADY_EXIST("user already exist");

    private final String message;
}
