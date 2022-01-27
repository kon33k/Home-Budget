package com.kon.budget.exception;

import lombok.Getter;

@Getter
public class MissingExpensesFilterException extends RuntimeException {

    private String errorCode;

    public MissingExpensesFilterException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
