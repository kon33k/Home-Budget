package com.kon.budget.validator;

import com.kon.budget.enums.ValidatorsAssetEnum;
import com.kon.budget.service.dtos.AssetDto;

import java.util.Objects;

public class AmountValidator implements Validator {

    private Validator next = new IncomeDataValidator();

    @Override
    public ValidatorMessage valid(AssetDto dto, ValidatorMessage validatorMessage) {

        if (Objects.isNull(dto.getAmount())) {
            validatorMessage.getMessage().add("no amount");
            validatorMessage.getCode().add("random UIDD");
        }
        return next.valid(dto, validatorMessage);
    }
}
