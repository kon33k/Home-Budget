package com.kon.budget.validator;

import com.kon.budget.enums.ValidatorsAssetEnum;
import com.kon.budget.service.dtos.AssetDto;

import java.util.Objects;

public class AmountValidator implements Validator {

    /*
    sprawcza czy wartosc amount jest poprawnie wpisana w json
    message brany z enuma
    kod bledu to rancom ULIC
     */

    //dodaje pole zeby wszysgtkie bledene wpisane dane wyswietały sie razem
    private Validator next = new IncomeDataValidator();

    @Override
    public ValidatorMessage valid(AssetDto dto, ValidatorMessage validatorMessage) {

        if (Objects.isNull(dto.getAmount())) {
            validatorMessage.getMessage().add(ValidatorsAssetEnum.NO_AMOUNT.getMessage());
            validatorMessage.getCode().add("01FSPDG6AWK160CEVJQHEX348M");
        }
        return next.valid(dto, validatorMessage);
    }
}
