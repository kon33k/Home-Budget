package com.kon.budget.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum FilterExceptionErrorMessages {

    MISSING_EXPENSES_FILTER_KEY("Missing filter key for Expenses: "),
    MISSING_ASSETS_FILTER_KEY("Missing filter ket for Assets: ");

    private final String message;

    public String getMessage(String missingKey) {
        return this.message + missingKey;
    }
}
