package com.kon.budget.builder;

import com.kon.budget.repository.entities.AssetEntity;

import java.math.BigDecimal;
import java.util.UUID;

public class AssetEntityBuilder {

    private UUID id;
    private BigDecimal amount;


    public AssetEntityBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public AssetEntityBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public AssetEntity build() {
        var dto = new AssetEntity();
        dto.setId(this.id);
        dto.setAmount(this.amount);
        return dto;
    }
}
