package com.kon.budget.repository.entities;

import com.kon.budget.enums.AssetCategory;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "assets")
@Getter
@Setter
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

    private LocalDateTime incomeDate;

    @Enumerated(EnumType.STRING)
    private AssetCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssetEntity entity = (AssetEntity) o;
        return Objects.equals(id, entity.id) && Objects.equals(user, entity.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user);
    }
}
