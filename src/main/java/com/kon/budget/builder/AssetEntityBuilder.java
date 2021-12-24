package com.kon.budget.builder;

import com.kon.budget.enums.AssetCategory;
import com.kon.budget.repository.entities.AssetEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class AssetEntityBuilder {

    private UUID id;
    private BigDecimal amount;
    private Instant incomeDate;
    private AssetCategory category;


    public AssetEntityBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public AssetEntityBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public AssetEntityBuilder withIncomeDate(Instant incomeDate) {
        this.incomeDate = incomeDate;
        return this;
    }

    public AssetEntityBuilder withCategory(AssetCategory category) {
        this.category = category;
        return this;
    }

    public AssetEntity build() {
        var entity = new AssetEntity();
        entity.setId(this.id);
        entity.setAmount(this.amount);
        entity.setIncomeDate(this.incomeDate);
        entity.setCategory(this.category);
        return entity;
    }
}
