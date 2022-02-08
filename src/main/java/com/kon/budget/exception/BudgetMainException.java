package com.kon.budget.exception;

import lombok.Getter;

@Getter
public class BudgetMainException extends RuntimeException {

    private final String errorCode;

    public BudgetMainException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
