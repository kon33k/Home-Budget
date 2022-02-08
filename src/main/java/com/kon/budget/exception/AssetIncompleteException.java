package com.kon.budget.exception;

import lombok.Getter;

@Getter
public class AssetIncompleteException extends BudgetMainException {

    public AssetIncompleteException(String message, String errorCode) {
        super(message, errorCode);
    }

}
