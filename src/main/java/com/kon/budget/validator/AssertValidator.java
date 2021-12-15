package com.kon.budget.validator;

import com.kon.budget.enums.ValidatorsAssetEnum;
import com.kon.budget.exception.AssertIncompleteException;
import com.kon.budget.service.dtos.AssetDto;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AssertValidator {

    public void validate(AssetDto dto) {
        if(Objects.isNull(dto.getAmount()))
            throw new AssertIncompleteException(ValidatorsAssetEnum.NO_AMOUNT.getMessage());
    }
}
