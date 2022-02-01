package com.kon.budget.repository;


import com.kon.budget.enums.AssetCategory;
import com.kon.budget.repository.entities.AssetEntity;
import com.kon.budget.repository.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AssetsRepository extends JpaRepository<AssetEntity, UUID> {

    List<AssetEntity> getAssetEntitiesByCategory(AssetCategory category);

    List<AssetEntity> getAssetEntitiesByUser(UserEntity userEntity);

    void deleteByUser(UserEntity userEntity);

    @Query("SELECT e FROM AssetEntity e WHERE e.user = :user AND e.incomeDate >= :fromDate AND e.incomeDate <= :toDate")
    List<AssetEntity> findAllByBetweenDate(UserEntity user, LocalDateTime fromDate, LocalDateTime toDate);
}
