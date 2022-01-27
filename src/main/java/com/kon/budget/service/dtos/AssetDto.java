package com.kon.budget.service.dtos;

import com.kon.budget.enums.AssetCategory;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class AssetDto {

    private UUID id;
    private BigDecimal amount;
    private LocalDateTime incomeDate;
    private AssetCategory category;
}
