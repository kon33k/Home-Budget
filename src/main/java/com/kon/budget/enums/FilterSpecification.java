package com.kon.budget.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FilterSpecification {

    FOR_ASSETS("for assets validator", "for assets range"),
    FOR_EXPENSES("for expenses validator", "for expenses range");

    private final String forValidator;
    private final String forRange;
}
