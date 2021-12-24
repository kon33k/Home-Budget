package com.kon.budget.repository.entities;

import com.kon.budget.enums.AssetCategory;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "assets")
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class AssetEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
    private BigDecimal amount;
    private Instant incomeDate;
    private AssetCategory category;
}
