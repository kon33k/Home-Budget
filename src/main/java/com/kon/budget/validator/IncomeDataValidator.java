package com.kon.budget.validator;

import com.kon.budget.enums.ValidatorsAssetEnum;
import com.kon.budget.exception.AssetIncompleteException;
import com.kon.budget.service.dtos.AssetDto;

import java.util.Objects;

class IncomeDataValidator implements Validator{

    /*
    sprawcza czy wartosc incomeDate jest poprawnie wpisana w json
    message brany z enuma
    kod bledu to rancom ULIC
     */

    @Override
    public ValidatorMessage valid(AssetDto dto, ValidatorMessage validatorMessage) {
        if (Objects.isNull(dto.getIncomeDate())) {
            validatorMessage.getMessage().add(ValidatorsAssetEnum.NO_INCOME_DATE.getMessage());
            validatorMessage.getCode().add("01FSPDDVVGHH9A7D4X6P4181WH");
        }
        return validatorMessage;
    }
}
