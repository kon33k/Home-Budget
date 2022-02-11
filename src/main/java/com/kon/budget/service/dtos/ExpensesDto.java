package com.kon.budget.service.dtos;

import com.kon.budget.enums.ExpensesCategory;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpensesDto {

    private UUID id;
    private BigDecimal amount;
    private LocalDateTime purchaseData;
    private ExpensesCategory category;
}
