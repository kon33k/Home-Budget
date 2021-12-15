package com.kon.budget.service;

import com.kon.budget.mapper.AssetsMapper;
import com.kon.budget.repository.AssetsRepository;
import com.kon.budget.service.dtos.AssetDto;
import com.kon.budget.service.dtos.AssetsDto;
import com.kon.budget.validator.AssertValidator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@Service
public class AssetsService {

    private AssetsRepository assetsRepository;
    private AssetsMapper assetsMapper;
    private AssertValidator assertValidator;

    public AssetsService(AssetsRepository assetsRepository, AssetsMapper assetsMapper, AssertValidator assertValidator) {
        this.assetsRepository = assetsRepository;
        this.assetsMapper = assetsMapper;
        this.assertValidator = assertValidator;
    }

    public AssetsDto getAllAssets() {
        var assetsEntity = assetsRepository.findAll();
        var assetsAmount = assetsEntity.stream()
                .map(entity -> entity.getAmount().intValue())
                .collect(Collectors.toList());
        var dto = new AssetsDto();
        dto.setAssets(assetsAmount);
        return dto;
    }

    public void setAsset(AssetDto dto) {
        assertValidator.validate(dto);
        var entity = assetsMapper.fromDtoToEntity(dto);

        assetsRepository.save(entity);
    }

    public void deleteAsset(AssetDto dto) {
        var entirt = assetsMapper.fromDtoToEntity(dto);

        assetsRepository.delete(entirt);
    }
}
