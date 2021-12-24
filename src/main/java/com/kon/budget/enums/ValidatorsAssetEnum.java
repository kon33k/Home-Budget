package com.kon.budget.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ValidatorsAssetEnum {

    NO_AMOUNT("no amount"),
    NO_INCOME_DATE("no income date");

    private final String message;

}
