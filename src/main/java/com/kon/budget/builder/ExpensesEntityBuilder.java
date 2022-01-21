package com.kon.budget.builder;

import com.kon.budget.enums.AssetCategory;
import com.kon.budget.enums.ExpensesCategory;
import com.kon.budget.repository.entities.AssetEntity;
import com.kon.budget.repository.entities.ExpensesEntity;
import com.kon.budget.repository.entities.UserEntity;


import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class ExpensesEntityBuilder {

    private UUID id;
    private BigDecimal amount;
    private Instant purchaseDate;
    private UserEntity user;
    private ExpensesCategory category;

    public ExpensesEntityBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public ExpensesEntityBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public ExpensesEntityBuilder withIncomeDate(Instant purchaseDate) {
        this.purchaseDate = purchaseDate;
        return this;
    }

    public ExpensesEntityBuilder withCategory(ExpensesCategory category) {
        this.category = category;
        return this;
    }

    public ExpensesEntityBuilder withUser(UserEntity user) {
        this.user = user;
        return this;
    }

    public ExpensesEntity build() {
        var entity = new ExpensesEntity();
        entity.setId(this.id);
        entity.setAmount(this.amount);
        entity.setPurchaseDate(this.purchaseDate);
        entity.setCategory(this.category);
        entity.setUser(this.user);
        return entity;
    }
}
