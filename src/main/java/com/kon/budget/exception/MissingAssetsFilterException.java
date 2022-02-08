package com.kon.budget.exception;

import lombok.Getter;

@Getter
public class MissingAssetsFilterException extends BudgetMainException {

    public MissingAssetsFilterException(String message, String errorCode) {
        super(message, errorCode);
    }
}
