package com.kon.budget.validator;

import com.kon.budget.enums.FilterExceptionErrorMessages;
import com.kon.budget.exception.MissingAssetsFilterException;
import org.springframework.stereotype.Component;

@Component
public class AssetsFilterParametersValidator extends FilterParametersValidator {

    @Override
    public void throwException(String missingKey, String errorCode) {
        throw new MissingAssetsFilterException(
                FilterExceptionErrorMessages.MISSING_ASSETS_FILTER_KEY.getMessage(missingKey),
                errorCode);
    }
}
