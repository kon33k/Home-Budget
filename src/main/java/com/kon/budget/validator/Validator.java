package com.kon.budget.validator;

import com.kon.budget.service.dtos.AssetDto;

@FunctionalInterface
public interface Validator {

    ValidatorMessage valid(AssetDto dto, ValidatorMessage validatorMessage);
}
