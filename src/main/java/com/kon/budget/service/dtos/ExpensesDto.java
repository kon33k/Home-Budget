package com.kon.budget.service.dtos;

import com.kon.budget.enums.ExpensesCategory;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
public class ExpensesDto {

    private UUID id;
    private BigDecimal amount;
    private LocalDateTime purchaseData;
    private ExpensesCategory category;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpensesDto that = (ExpensesDto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
