package com.kon.budget.builder;

import com.kon.budget.enums.ExpensesCategory;
import com.kon.budget.service.dtos.ExpensesDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class ExpensesDtoBuilder {

    private UUID id;
    private BigDecimal amount;
    private Instant purchaseData;
    private ExpensesCategory category;

    public ExpensesDtoBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public ExpensesDtoBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public ExpensesDtoBuilder purchaseData(Instant purchaseData) {
        this.purchaseData = purchaseData;
        return this;
    }

    public ExpensesDtoBuilder withCategory(ExpensesCategory category) {
        this.category = category;
        return this;
    }

    public ExpensesDto build() {
        var dto = new ExpensesDto();
        dto.setId(this.id);
        dto.setAmount(this.amount);
        dto.setPurchaseData(this.purchaseData);
        dto.setCategory(this.category);
        return dto;
    }
}
