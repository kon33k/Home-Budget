package com.kon.budget.exception;

import lombok.Getter;

@Getter
public class MissingAssetsFilterException extends RuntimeException {

    private final String errorCode;

    public MissingAssetsFilterException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
