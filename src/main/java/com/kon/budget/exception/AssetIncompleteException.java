package com.kon.budget.exception;

import lombok.Getter;

@Getter
public class AssetIncompleteException extends RuntimeException {

    /**
    * wyjątek do niewłasciwie wysłanego json
     */

    private final String errorCode;

    public AssetIncompleteException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
