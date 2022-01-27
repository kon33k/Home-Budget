package com.kon.budget.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExpensesExceptionErrorMessages {

    MISSING_FILTER_KEY("Missing filter key: ");

    private final String message;
}
