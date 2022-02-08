package com.kon.budget.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FilterParameterCalendarEnum {
    FROM_DATE("from"),
    TO_DATE("to"),
    YEAR("year"),
    MONTH("month"),
    CATEGORY("category");

    private final String key;

}
