package com.kon.budget.mapper;

import com.kon.budget.repository.entities.AssetEntity;
import com.kon.budget.repository.entities.UserEntity;
import com.kon.budget.service.dtos.AssetDto;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AssetsMapper {

    /**
    * sprawdza czy dto lub któreś z pól dto nie jest null i ustawia pola w AssetEntityBuilder
     */

    // TODO: przerobic na mapstruct
    public AssetEntity fromDtoToEntity(AssetDto dto, UserEntity user) {

        if(Objects.isNull(dto)) {
            return null;
        }

        var entityBuilder = AssetEntity.builder();

        if(Objects.nonNull(dto.getAmount())) {
            entityBuilder.amount(dto.getAmount());
        }

        if(Objects.nonNull(dto.getId())) {
            entityBuilder.id(dto.getId());
        }

        if(Objects.nonNull(dto.getIncomeDate()))
            entityBuilder.incomeDate(dto.getIncomeDate());

        if(Objects.nonNull(dto.getCategory())) {
            entityBuilder.category(dto.getCategory());
        }

        if(Objects.nonNull(dto.getDescription())) {
            entityBuilder.description(dto.getDescription());
        }

        if(Objects.nonNull(user)) {
            entityBuilder.user(user);
        }

        return entityBuilder.build();
    }

    /*
    sprawdza czy encja lub któreś z pól encji nie jest null i ustawia pola w AssetDtoBuilder
     */

    public AssetDto fromEntityToDto(AssetEntity entity) {
        if (Objects.isNull(entity)) {
            return null;
        }

        var dtoBuilder = AssetDto.builder();

        if (Objects.nonNull(entity.getAmount())) {
            dtoBuilder.amount(entity.getAmount());
        }

        if (Objects.nonNull(entity.getId())) {
            dtoBuilder.id(entity.getId());
        }

        if (Objects.nonNull(entity.getIncomeDate())) {
            dtoBuilder.incomeDate(entity.getIncomeDate());
        }

        if (Objects.nonNull(entity.getDescription())) {
            dtoBuilder.description(entity.getDescription());
        }

        if (Objects.nonNull(entity.getCategory())) {
            dtoBuilder.category(entity.getCategory());
        }

        return dtoBuilder.build();

    }
}
