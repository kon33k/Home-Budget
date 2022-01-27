package com.kon.budget.validator;

import java.util.Map;

public abstract class FilterParametersValidator {

    public void assertFilter(Map<String, String> filter) {

    };

    public abstract void throwException();
}
