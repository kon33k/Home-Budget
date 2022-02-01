package com.kon.budget.validator;

import com.kon.budget.enums.FilterExceptionErrorMessages;
import com.kon.budget.exception.MissingExpensesFilterException;
import org.springframework.stereotype.Component;

@Component
public class ExpensesFilterParametersValidator extends FilterParametersValidator {
    @Override
    public void throwException(String missingKey, String errorCode) {
        throw new MissingExpensesFilterException(
                FilterExceptionErrorMessages.MISSING_EXPENSES_FILTER_KEY.getMessage(missingKey),  errorCode);
    }
}
