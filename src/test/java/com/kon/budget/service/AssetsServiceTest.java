package com.kon.budget.service;

import com.kon.budget.builder.AssetDtoBuilder;
import com.kon.budget.builder.AssetEntityBuilder;
import com.kon.budget.enums.ValidatorsAssetEnum;
import com.kon.budget.exception.AssertIncompleteException;
import com.kon.budget.mapper.AssetsMapper;
import com.kon.budget.repository.AssetsRepository;
import com.kon.budget.repository.entities.AssetEntity;
import com.kon.budget.service.dtos.AssetDto;
import com.kon.budget.validator.AssertValidator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AssetsServiceTest {


    @Mock
    private AssetsRepository assetsRepository;
    private AssertValidator assertValidator = new AssertValidator();
    private AssetsMapper assetsMapper = new AssetsMapper();

    private AssetsService service;

    @BeforeEach
    public void init() {
        service = new AssetsService(assetsRepository, assetsMapper, assertValidator);
    }

    @Test
    void shouldSaveAssetAndReturnListWithOneElementIfThereWasNoSavedAssetsBefore() {
        //given
        var asset = 1;
        AssetEntity assetEntity = new AssetEntityBuilder()
            .withAmount(new BigDecimal(asset))
            .build();
        List<AssetEntity> assetList = Collections.singletonList(assetEntity);
        Mockito.when(assetsRepository.findAll()).thenReturn(assetList);
        //when
        var result = service.getAllAssets();
        //then
        var listOfAssets = result.getAssets();
        Assertions.assertThat(listOfAssets)
                .hasSize(1)
                .containsExactly(asset);
    }

    @Test
    void shouldSaveAssetAndReturnListWithTwoElementsIfThereWasNoSavedAssetsBefore() {
        //given
        var assetOne = 1;
        var assetTwo = 2;
        AssetEntity entityOne = new AssetEntityBuilder()
                .withAmount(new BigDecimal(assetOne))
                .build();
        AssetEntity entityTwo = new AssetEntityBuilder()
                .withAmount(new BigDecimal(assetTwo))
                .build();
        List<AssetEntity> assetEntities = asList(entityOne, entityTwo);
        Mockito.when(assetsRepository.findAll()).thenReturn(assetEntities);

        //when
        var result = service.getAllAssets();
        //then
        var listOfAsset = result.getAssets();
        Assertions.assertThat(listOfAsset)
                .hasSize(2)
                .containsExactly(assetOne, assetTwo);
    }

    @Test
    void shouldVerifyIfTheRepositorySaveWasCalledOneTime() {
        //given
        BigDecimal asset = BigDecimal.ONE;
        AssetDto dto = new AssetDtoBuilder()
                .withAmount(asset)
                .build();
        AssetEntity entity = new AssetEntityBuilder()
                .withAmount(asset)
                .build();
        //when
        service.setAsset(dto);
        //then
        Mockito.verify(assetsRepository, Mockito.times(1)).save(entity);
    }

    @Test
    void shouldThrowExceptionWhenAmountInAssetDtoIsNull() {
        //given
        AssetDto dto = new AssetDto();
        //when
        var result =  assertThrows(AssertIncompleteException.class,
                () -> service.setAsset(dto));
        //then
        assertEquals(ValidatorsAssetEnum.NO_AMOUNT.getMessage(), result.getMessage());
    }
}