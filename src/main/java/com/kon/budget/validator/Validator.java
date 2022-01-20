package com.kon.budget.validator;

import com.kon.budget.service.dtos.AssetDto;

@FunctionalInterface
public interface Validator {

    /*
    zwraca klase pomocnicz ValidatorMessage
     */

    ValidatorMessage valid(AssetDto dto, ValidatorMessage validatorMessage);
}
