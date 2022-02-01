package com.kon.budget.validator;

import com.kon.budget.enums.FilterParameterCalendarEnum;

import java.util.Map;

public abstract class FilterParametersValidator {

    /**
     * walidacja filtrów, error code to random ULID
     */

    public void assertFilter(Map<String, String> filter) {
        checkIfFromDateExistToDateIsMissing(filter, "01FTBQPDCDN0H07Z7RX4JKF1BE");
        chceckifToDateExistFromDateIsMissing(filter, "01FTBQSXRGNQ60WWYE2ENE953W");
        checkIfYearExistAndMonthIsMissing(filter, "01FTBRZ5MR8V14HBZT72E4D9AY");
        checkIfMonthExistAndYearIsMissing(filter, "01FTBS133FXBQJJA82Q1WJAX9C");

    }

    protected void checkIfMonthExistAndYearIsMissing(Map<String, String> filter, String errorCode) {
        if(filter.containsKey(FilterParameterCalendarEnum.MONTH.getKey())
                && !filter.containsKey(FilterParameterCalendarEnum.YEAR.getKey())) {
            throwException(FilterParameterCalendarEnum.YEAR.getKey(), errorCode);
        }
    }

    protected void checkIfYearExistAndMonthIsMissing(Map<String, String> filter, String errorCode) {
        if(filter.containsKey(FilterParameterCalendarEnum.YEAR.getKey())
                && !filter.containsKey(FilterParameterCalendarEnum.MONTH.getKey())) {
            throwException(FilterParameterCalendarEnum.MONTH.getKey(), errorCode);
        }
    }

    protected void chceckifToDateExistFromDateIsMissing(Map<String, String> filter, String errorCode) {
        if (filter.containsKey(FilterParameterCalendarEnum.TO_DATE.getKey())
                && !filter.containsKey(FilterParameterCalendarEnum.FROM_DATE.getKey())) {
            throwException(FilterParameterCalendarEnum.FROM_DATE.getKey(), errorCode);
        }
    }

    protected void checkIfFromDateExistToDateIsMissing(Map<String, String> filter, String errorCode) {
        if(filter.containsKey(FilterParameterCalendarEnum.FROM_DATE.getKey())
                 &&!filter.containsKey(FilterParameterCalendarEnum.TO_DATE.getKey())) {
            throwException(FilterParameterCalendarEnum.TO_DATE.getKey(), errorCode);
        }
    }


    public abstract void throwException(String missingKey, String errorCode);
}

