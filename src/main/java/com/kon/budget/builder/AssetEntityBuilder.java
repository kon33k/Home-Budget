package com.kon.budget.builder;

import com.kon.budget.enums.AssetCategory;
import com.kon.budget.repository.entities.AssetEntity;
import com.kon.budget.repository.entities.UserEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public class AssetEntityBuilder {

    /*
    buduje AssetEntity
     */

    private UUID id;
    private BigDecimal amount;
    private LocalDateTime incomeDate;
    private AssetCategory category;
    private UserEntity userEntity;


    public AssetEntityBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public AssetEntityBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public AssetEntityBuilder withIncomeDate(LocalDateTime incomeDate) {
        this.incomeDate = incomeDate;
        return this;
    }

    public AssetEntityBuilder withCategory(AssetCategory category) {
        this.category = category;
        return this;
    }


    public AssetEntityBuilder withUser(UserEntity userEntity) {
        this.userEntity = userEntity;
        return this;
    }

    public AssetEntity build() {
        var entity = new AssetEntity();
        entity.setId(this.id);
        entity.setAmount(this.amount);
        entity.setIncomeDate(this.incomeDate);
        entity.setCategory(this.category);
        entity.setUser(this.userEntity);
        return entity;
    }

}
