package com.kon.budget.validator;

import com.kon.budget.enums.ValidatorsAssetEnum;
import com.kon.budget.exception.AssetIncompleteException;
import com.kon.budget.service.dtos.AssetDto;

import java.util.Objects;

class IncomeDataValidator implements Validator{

    @Override
    public ValidatorMessage valid(AssetDto dto, ValidatorMessage validatorMessage) {
        if (Objects.isNull(dto.getIncomeDate())) {
            validatorMessage.getMessage().add(ValidatorsAssetEnum.NO_INCOME_DATE.getMessage());
            validatorMessage.getCode().add("random UIDD");
        }
        return validatorMessage;
    }
}
