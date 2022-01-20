package com.kon.budget.service;

import com.kon.budget.builder.AssetDtoBuilder;
import com.kon.budget.builder.AssetEntityBuilder;
import com.kon.budget.enums.ValidatorsAssetEnum;
import com.kon.budget.exception.AssetIncompleteException;
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
import java.time.Instant;
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
    private final AssetValidator assetValidator = new AssetValidator();
    private final AssetsMapper assetsMapper = new AssetsMapper();

    private AssetsService service;

    @BeforeEach
    public void init() {
        service = new AssetsService(assetsRepository, assetsMapper, assetValidator, userLogInfoService);
    }

    @Test
    void shouldSaveAssetAndReturnListWithOneElementIfThereWasNoSavedAssetsBefore() {
        //given
        var asset = BigDecimal.ONE;
        AssetEntity assetEntity = new AssetEntityBuilder()
            .withAmount(asset)
            .build();
        List<AssetEntity> assetList = Collections.singletonList(assetEntity);
        Mockito.when(assetsRepository.getAssetEntitiesByUser(any())).thenReturn(assetList);
        //when
        var result = service.getAllAssets();
        //then
        Assertions.assertThat(result)
                .hasSize(1)
                .contains(new AssetDtoBuilder().withAmount(asset).build());
    }

    @Test
    void shouldSaveAssetAndReturnListWithTwoElementsIfThereWasNoSavedAssetsBefore() {
        //given
        var assetOne = BigDecimal.ONE;
        var assetTwo = new BigDecimal("2");
        AssetEntity entityOne = new AssetEntityBuilder()
                .withAmount(assetOne)
                .build();
        AssetEntity entityTwo = new AssetEntityBuilder()
                .withAmount(assetTwo)
                .build();
        List<AssetEntity> assetEntities = asList(entityOne, entityTwo);
        Mockito.when(assetsRepository.getAssetEntitiesByUser(any())).thenReturn(assetEntities);

        //when
        var result = service.getAllAssets();
        //then
        Assertions.assertThat(result)
                .hasSize(2)
                .containsExactly(
                        new AssetDtoBuilder().withAmount(assetOne).build(),
                        new AssetDtoBuilder().withAmount(assetTwo).build()
                );
    }

    @Test
    void shouldVerifyIfTheRepositorySaveWasCalledOneTime() {
        //given
        BigDecimal asset = BigDecimal.ONE;
        Instant incomeDate = Instant.now();
        AssetDto dto = new AssetDtoBuilder()
                .withAmount(asset)
                .withIncomeDate(incomeDate)
                .build();
        AssetEntity entity = new AssetEntityBuilder()
                .withAmount(asset)
                .withIncomeDate(incomeDate)
                .build();
        //when
        service.setAsset(dto);
        //then
        Mockito.verify(assetsRepository, Mockito.times(1)).save(entity);
    }

    @Test
    void shouldThrowExceptionWhenAmountInAssetDtoIsNull() {
        //given
        AssetDto dto = new AssetDtoBuilder()
                .withIncomeDate(Instant.now())
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
        var dto = new AssetDtoBuilder().withAmount(asset).build();

        var entity = new AssetEntityBuilder().withAmount(asset).build();
        Mockito.when(assetsRepository.findById(any())).thenReturn(Optional.of(entity));
        //when
        service.updateAsset(dto);
        //then
        Mockito.verify(assetsRepository, Mockito.times(1)).saveAndFlush(entity);
    }
}