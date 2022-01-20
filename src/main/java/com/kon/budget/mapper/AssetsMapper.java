package com.kon.budget.mapper;

import com.kon.budget.builder.AssetDtoBuilder;
import com.kon.budget.builder.AssetEntityBuilder;
import com.kon.budget.repository.entities.AssetEntity;
import com.kon.budget.repository.entities.UserEntity;
import com.kon.budget.service.dtos.AssetDto;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AssetsMapper {

    /*
    sprawdza czy dto lub któreś z pól dto nie jest null i ustawia pola w AssetEntityBuilder
     */

    public AssetEntity fromDtoToEntity(AssetDto dto, UserEntity user) {

        if(Objects.isNull(dto)) {
            return null;
        }

        var entityBuilder = new AssetEntityBuilder();

        if(Objects.nonNull(dto.getAmount())) {
            entityBuilder.withAmount(dto.getAmount());
        }

        if(Objects.nonNull(dto.getId())) {
            entityBuilder.withId(dto.getId());
        }

        if(Objects.nonNull(dto.getIncomeDate()))
            entityBuilder.withIncomeDate(dto.getIncomeDate());

        if(Objects.nonNull(dto.getCategory())) {
            entityBuilder.withCategory(dto.getCategory());
        }

        if(Objects.nonNull(user)) {
            entityBuilder.withUser(user);
        }

        return entityBuilder.build();
    }

    /*
    sprawdza czy encja lub któreś z pól encji nie jest null i ustawia pola w AssetDtoBuilder
     */

    public AssetDto fromEntityToDto(AssetEntity entity) {

        if(Objects.isNull(entity)) {
            return null;
        }

        var dtoBuilder = new AssetDtoBuilder();

        if(Objects.nonNull(entity.getAmount())) {
            dtoBuilder.withAmount(entity.getAmount());
        }

        if(Objects.nonNull(entity.getId())) {
            dtoBuilder.withId(entity.getId());
        }

        if(Objects.nonNull((entity.getIncomeDate()))) {
            dtoBuilder.withIncomeDate(entity.getIncomeDate());
        }

        if(Objects.nonNull(entity.getIncomeDate())) {
            dtoBuilder.withCategory(entity.getCategory());
        }


        return dtoBuilder.build();
    }
}
