package com.kon.budget.mapper;

import com.kon.budget.repository.entities.ExpensesEntity;
import com.kon.budget.repository.entities.UserEntity;
import com.kon.budget.service.dtos.ExpensesDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ExpensesMapper {

    /**
     * po sprawdzeiu czy pola nie sa null mapuje dto na encje
     */

    //TODO: przerobic na map struct

    public ExpensesEntity fromDtoToEntity(ExpensesDto dto, UserEntity user) {

        var entity = new ExpensesEntity();

        if(Objects.nonNull(dto.getCategory())) {
            entity.setCategory(dto.getCategory());
        }

        if(Objects.nonNull(dto.getAmount()))
            entity.setAmount(dto.getAmount());


        if(Objects.nonNull(dto.getPurchaseData())) {
            entity.setCategory(dto.getCategory());
        }

        if(Objects.nonNull(dto.getId())) {
            entity.setId(dto.getId());
        }

        if(Objects.nonNull(user)) {
            entity.setUser(user);
        }

        return entity;
    }

    public ExpensesDto fromEntityToDtos(ExpensesEntity entity) {

        var dto = new ExpensesDto();

        if(Objects.nonNull(entity.getPurchaseDate())) {
            dto.setPurchaseData(entity.getPurchaseDate());
        }

        if(Objects.nonNull(entity.getId())) {
            dto.setId(entity.getId());
        }

        if(Objects.nonNull(entity.getAmount()))
            dto.setAmount(entity.getAmount());

        if(Objects.nonNull(entity.getCategory())) {
            dto.setCategory(entity.getCategory());
        }

        return dto;
    }

    public List<ExpensesDto> fromEntitiesToDtos(List<ExpensesEntity> allExpenses) {
        return allExpenses.stream()
                .map(this::fromEntityToDtos)
                .collect(Collectors.toList());
    }
}
