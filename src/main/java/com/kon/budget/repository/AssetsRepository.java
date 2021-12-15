package com.kon.budget.repository;


import com.kon.budget.repository.entities.AssetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AssetsRepository extends JpaRepository<AssetEntity, UUID> {
}
