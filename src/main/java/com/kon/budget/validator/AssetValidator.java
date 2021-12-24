package com.kon.budget.validator;

import com.kon.budget.enums.ValidatorsAssetEnum;
import com.kon.budget.exception.AssetIncompleteException;
import com.kon.budget.service.dtos.AssetDto;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class AssetValidator {

    private Validator validator = new AmountValidator();

    public void validate(AssetDto dto) {
        var validatorMessage = validator.valid(dto, new ValidatorMessage());

        if (validatorMessage.getMessage().isEmpty()) {
            return;
        }

        throw new AssetIncompleteException(validatorMessage.getMessage().toString(), validatorMessage.getCode().toString());
    }
}
