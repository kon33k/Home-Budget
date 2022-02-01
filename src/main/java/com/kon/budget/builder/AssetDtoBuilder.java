package com.kon.budget.builder;

import com.kon.budget.enums.AssetCategory;
import com.kon.budget.service.dtos.AssetDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class AssetDtoBuilder {

    /*
    buduje AssetDto
     */

    private UUID id;
    private BigDecimal amount;
    private LocalDateTime incomeDate;
    private AssetCategory category;


    public AssetDtoBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public AssetDtoBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public AssetDtoBuilder withIncomeDate(LocalDateTime incomeDate) {
        this.incomeDate = incomeDate;
        return this;
    }

    public AssetDtoBuilder withCategory(AssetCategory category) {
        this.category = category;
        return this;
    }

    public AssetDto build() {
        var dto = new AssetDto();
        dto.setId(this.id);
        dto.setAmount(this.amount);
        dto.setIncomeDate(this.incomeDate);
        dto.setCategory(this.category);
        return dto;
    }
}
