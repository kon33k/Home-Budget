package com.kon.budget.service;

import com.kon.budget.enums.AssetCategory;
import com.kon.budget.enums.FilterSpecification;
import com.kon.budget.filters.FilterRangeStrategy;
import com.kon.budget.mapper.AssetsMapper;
import com.kon.budget.repository.AssetsRepository;
import com.kon.budget.repository.UserRepository;
import com.kon.budget.repository.entities.AssetEntity;
import com.kon.budget.repository.entities.UserEntity;
import com.kon.budget.service.dtos.AssetDto;
import com.kon.budget.validator.AssetValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AssetsService {

    public static final Logger LOGGER = LoggerFactory.getLogger(AssetsService.class.getName());

    private final AssetsRepository assetsRepository;
    private final AssetsMapper assetsMapper;
    private final AssetValidator assetValidator;
    private final UserLogInfoService userLogInfoService;
    private final FilterRangeStrategy<AssetEntity> filterRangeStrategy;

    public AssetsService(AssetsRepository assetsRepository,
                         AssetsMapper assetsMapper,
                         AssetValidator assetValidator,
                         UserLogInfoService userLogInfoService,
                         FilterRangeStrategy filterRangeStrategy) {
        this.assetsRepository = assetsRepository;
        this.assetsMapper = assetsMapper;
        this.assetValidator = assetValidator;
        this.userLogInfoService = userLogInfoService;
        this.filterRangeStrategy = filterRangeStrategy;
    }

    public List<AssetDto> getAllAssets() {
        LOGGER.debug("Get all assets");
        var user = getUserEntity();
        return assetsRepository.getAssetEntitiesByUser(user).stream()
                .map(assetsMapper::fromEntityToDto)
                .collect(Collectors.toList());
    }

    public void setAsset(AssetDto dto) {
        LOGGER.info("Set Asset");
        LOGGER.debug("AssetDto {} ", dto);
        assetValidator.validate(dto);
        var user = getUserEntity();
        var entity = assetsMapper.fromDtoToEntity(dto, user);
        assetsRepository.save(entity);
        LOGGER.info("Asset Saved");
    }

    public void setAsset(List<AssetDto> dtos) {
        dtos.forEach(this::setAsset);
    }

    public void deleteAsset(AssetDto dto) {
        LOGGER.info("Delete asset");
        LOGGER.debug("AssetDto {}", dto);
        UserEntity user = getUserEntity();
        var entity = assetsMapper.fromDtoToEntity(dto, user);

        assetsRepository.delete(entity);
        LOGGER.info("Asset deleted");
    }

    @Transactional
    public void updateAsset(AssetDto dto) {
        LOGGER.info("Updated asset");
        LOGGER.debug("AssetDto {} ", dto);
        var entity = assetsRepository.findById(dto.getId());
        entity.ifPresent(e -> {
            e.setAmount(dto.getAmount());
            e.setDescription(dto.getDescription());
            assetsRepository.saveAndFlush(e);
        });
        LOGGER.info("Asset updated");
    }

    public List<AssetDto> getAssetsByCategory(AssetCategory category) {
        return assetsRepository.getAssetEntitiesByCategory(category)
                .stream().map(assetsMapper::fromEntityToDto)
                .collect(Collectors.toList());
    }

    // pobier aktualnie zalogowane usera
    private UserEntity getUserEntity() {
        LOGGER.info("GetLoggedUserEntity");
        return userLogInfoService.getLoggedUserEntity();
    }

    public void deleteById(UUID id) {
        LOGGER.info("Delete asset By Id: {}", id);

        assetsRepository.deleteById(id);
    }

    public void deleteAllAssetsByUser(UserEntity userEntity) {
        LOGGER.info("Delete all assets by user: {} ", userEntity.getUsername());

        assetsRepository.deleteByUser(userEntity);
    }

    public List<AssetDto> getFilteredAssets(Map<String, String> filter) {
        var user = userLogInfoService.getLoggedUserEntity();
        FilterSpecification specification = FilterSpecification.FOR_ASSETS;

        return filterRangeStrategy.getFilteredDataFromSpecification(user, filter, specification)
                .stream().map(assetsMapper::fromEntityToDto)
                .collect(Collectors.toList());
    }
}
