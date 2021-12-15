package com.kon.budget.mapper;

import com.kon.budget.builder.AssetDtoBuilder;
import com.kon.budget.builder.AssetEntityBuilder;
import com.kon.budget.repository.entities.AssetEntity;
import com.kon.budget.service.dtos.AssetDto;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component //tworzy bean
public class AssetsMapper {

    public AssetEntity fromDtoToEntity(AssetDto dto) {

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

        return entityBuilder.build();
    }

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

        return dtoBuilder.build();
    }
}
