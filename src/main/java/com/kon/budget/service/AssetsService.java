package com.kon.budget.service;

import com.kon.budget.mapper.AssetsMapper;
import com.kon.budget.repository.AssetsRepository;
import com.kon.budget.service.dtos.AssetDto;
import com.kon.budget.validator.AssetValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssetsService {

    public static final Logger LOGGER = LoggerFactory.getLogger(AssetsService.class.getName());

    private AssetsRepository assetsRepository;
    private AssetsMapper assetsMapper;
    private AssetValidator assetValidator;

    public AssetsService(AssetsRepository assetsRepository, AssetsMapper assetsMapper, AssetValidator assetValidator) {
        this.assetsRepository = assetsRepository;
        this.assetsMapper = assetsMapper;
        this.assetValidator = assetValidator;
    }

    public List<AssetDto> getAllAssets() {
        LOGGER.debug("Get all assets");
        return assetsRepository.findAll().stream()
                .map(entity -> assetsMapper.fromEntityToDto(entity))
                .collect(Collectors.toList());
    }

    public void setAsset(AssetDto dto) {
        LOGGER.info("Set Asset");
        LOGGER.debug("AssetDto  " + dto);
        assetValidator.validate(dto);
        var entity = assetsMapper.fromDtoToEntity(dto);

        assetsRepository.save(entity);
        LOGGER.info("Asset Saved");
    }

    public void deleteAsset(AssetDto dto) {
        LOGGER.info("Delete asset");
        LOGGER.debug("AssetDto " + dto);
        var entirt = assetsMapper.fromDtoToEntity(dto);

        assetsRepository.delete(entirt);
        LOGGER.info("Asset deleted");

    }

    public void updateAsset(AssetDto dto) {
        LOGGER.info("Updated asset");
        LOGGER.debug("AssetDto " + dto);
        var entity = assetsRepository.findById(dto.getId());
        entity.ifPresent(e -> {
            e.setAmount(dto.getAmount());
            assetsRepository.saveAndFlush(e);
        });
        LOGGER.info("Asset updated");

    }
}
