package com.kon.budget.validator.filters;

import com.kon.budget.enums.FilterExceptionErrorMessages;
import com.kon.budget.exception.MissingAssetsFilterException;
import org.springframework.stereotype.Component;

@Component("for assets validator")
class AssetsFilterParametersValidator extends FilterParametersValidator {

    @Override
    public void throwException(String missingKey, String errorCode) {
        throw new MissingAssetsFilterException(
                FilterExceptionErrorMessages.MISSING_ASSETS_FILTER_KEY.getMessage(missingKey),
                errorCode);
    }
}
