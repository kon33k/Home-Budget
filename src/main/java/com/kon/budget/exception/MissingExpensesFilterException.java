package com.kon.budget.exception;

import lombok.Getter;

@Getter
public class MissingExpensesFilterException extends BudgetMainException {

    public MissingExpensesFilterException(String message, String errorCode) {
        super(message, errorCode);
    }
}
