package com.kon.budget.service;

import com.kon.budget.enums.ValidatorsAssetEnum;
import com.kon.budget.exception.AssetIncompleteException;
import com.kon.budget.filters.FilterRangeStrategy;
import com.kon.budget.mapper.AssetsMapper;
import com.kon.budget.repository.AssetsRepository;
import com.kon.budget.repository.entities.AssetEntity;
import com.kon.budget.service.dtos.AssetDto;
import com.kon.budget.validator.AssetValidator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class AssetsServiceTest {


    @Mock
    private AssetsRepository assetsRepository;
    @Mock
    private UserLogInfoService userLogInfoService;
    @Mock
    private FilterRangeStrategy filterRangeStrategy;

    private final AssetValidator assetValidator = new AssetValidator();
    private final AssetsMapper assetsMapper = new AssetsMapper();

    private AssetsService service;


    @BeforeEach
    public void init() {
        service = new AssetsService(assetsRepository, assetsMapper, assetValidator, userLogInfoService, filterRangeStrategy);
    }

    @Test
    void shouldSaveAssetAndReturnListWithOneElementIfThereWasNoSavedAssetsBefore() {
        //given
        var asset = BigDecimal.ONE;
        AssetEntity assetEntity = AssetEntity.builder()
            .amount(asset)
            .build();
        List<AssetEntity> assetList = Collections.singletonList(assetEntity);
        Mockito.when(assetsRepository.getAssetEntitiesByUser(any())).thenReturn(assetList);
        //when
        var result = service.getAllAssets();
        //then
        Assertions.assertThat(result)
                .hasSize(1)
                .contains(AssetDto.builder().amount(asset).build());
    }

    @Test
    void shouldSaveAssetAndReturnListWithTwoElementsIfThereWasNoSavedAssetsBefore() {
        //given
        var assetOne = BigDecimal.ONE;
        var assetTwo = new BigDecimal("2");
        AssetEntity entityOne = AssetEntity.builder()
                .amount(assetOne)
                .build();
        AssetEntity entityTwo = AssetEntity.builder()
                .amount(assetTwo)
                .build();
        List<AssetEntity> assetEntities = asList(entityOne, entityTwo);
        Mockito.when(assetsRepository.getAssetEntitiesByUser(any())).thenReturn(assetEntities);

        //when
        var result = service.getAllAssets();
        //then
        Assertions.assertThat(result)
                .hasSize(2)
                .containsExactly(
                        AssetDto.builder().amount(assetOne).build(),
                        AssetDto.builder().amount(assetTwo).build()
                );
    }

    @Test
    void shouldVerifyIfTheRepositorySaveWasCalledOneTime() {
        //given
        BigDecimal asset = BigDecimal.ONE;
        LocalDateTime incomeDate = LocalDateTime.now();
        AssetDto dto = AssetDto.builder()
                .amount(asset)
                .incomeDate(incomeDate)
                .build();
        AssetEntity entity = AssetEntity.builder()
                .amount(asset)
                .incomeDate(incomeDate)
                .build();
        //when
        service.setAsset(dto);
        //then
        Mockito.verify(assetsRepository, Mockito.times(1)).save(entity);
    }

    @Test
    void shouldThrowExceptionWhenAmountInAssetDtoIsNull() {
        //given
        AssetDto dto = AssetDto.builder()
                .incomeDate(LocalDateTime.now())
                .build();
        List<String> list = new ArrayList<>();
        list.add(ValidatorsAssetEnum.NO_AMOUNT.getMessage());
        //when
        var result = assertThrows(AssetIncompleteException.class,
            () -> service.setAsset(dto));
        //then
        assertEquals(list.toString(), result.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAmountAndIncomeDateInAssetDtoIsNull() {
        //given
        AssetDto dto = new AssetDto();

        List<String> list = new ArrayList<>();
        list.add(ValidatorsAssetEnum.NO_AMOUNT.getMessage());
        list.add(ValidatorsAssetEnum.NO_INCOME_DATE.getMessage());
        //when
        var result = assertThrows(AssetIncompleteException.class,
                () -> service.setAsset(dto));
        //then
        assertEquals(list.toString(), result.getMessage());
    }

    @Test
    void shouldVerifyIfTheRepositoryUpdateWasCalled() {
        //given
        BigDecimal asset = BigDecimal.ONE;
        var dto = AssetDto.builder()
                .amount(asset)
                .build();
        var entity = AssetEntity.builder()
                .amount(asset)
                .build();
        Mockito.when(assetsRepository.findById(any())).thenReturn(Optional.of(entity));
        //when
        service.updateAsset(dto);
        //then
        Mockito.verify(assetsRepository, Mockito.times(1)).saveAndFlush(entity);
    }
}