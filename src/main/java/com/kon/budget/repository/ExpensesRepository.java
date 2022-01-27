package com.kon.budget.repository;

import com.kon.budget.repository.entities.ExpensesEntity;
import com.kon.budget.repository.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ExpensesRepository extends JpaRepository<ExpensesEntity, UUID> {

    List<ExpensesEntity> findAllByUser(UserEntity user);

    @Query("SELECT e FROM ExpensesEntity e WHERE e.user = :user AND e.purchaseDate >= :fromDate AND e.purchaseDate <= :toDate")
    List<ExpensesEntity> findAllByBetweenDate(UserEntity user, LocalDateTime fromDate, LocalDateTime toDate);
}
