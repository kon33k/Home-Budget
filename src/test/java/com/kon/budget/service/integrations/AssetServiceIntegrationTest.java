package com.kon.budget.service.integrations;

import com.kon.budget.builder.AssetDtoBuilder;
import com.kon.budget.builder.AssetEntityBuilder;
import com.kon.budget.enums.AssetCategory;
import com.kon.budget.repository.AssetsRepository;
import com.kon.budget.repository.entities.AssetEntity;
import com.kon.budget.service.AssetsService;
import com.kon.budget.service.dtos.AssetDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;

import static java.util.Arrays.asList;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@Transactional
public class AssetServiceIntegrationTest {

    @Autowired
    private AssetsRepository assetsRepository;
    @Autowired
    private AssetsService assetsService;

    @Test
    void shouldReturnThreeAssets() {
        //given
        initDataBase();
        //when
        var allAssetsInDataBase = assetsService.getAllAssets();
        //then
        assertThat(allAssetsInDataBase).hasSize(3);
    }

    @Test
    void shouldAddAssetToDataBase() {
        //given
        AssetDto dto = new AssetDtoBuilder()
                .withAmount(new BigDecimal(1000))
                .withIncomeDate(Instant.now())
                .withCategory(AssetCategory.OTHER)
                .build();
        //when
        assetsService.setAsset(dto);
        //then
        var allAssetsInDataBase = assetsRepository.findAll();
        assertThat(allAssetsInDataBase).hasSize(1);
        var entity = allAssetsInDataBase.get(0);
        assertThat(entity.getAmount()).isEqualTo(dto.getAmount());
        assertThat(entity.getIncomeDate()).isEqualTo(dto.getIncomeDate());
        assertThat(entity.getCategory()).isEqualTo(dto.getCategory());
    }

    @Test
    void shouldReturnListWithOneCategory() {
        //given
        initDataBase();
        var category = AssetCategory.OTHER;
        //when
        var allAssetsWithOnceCategory = assetsService.getAssetsByCategory(category);
        //then
        assertThat(allAssetsWithOnceCategory).hasSize(2);
        var entity = allAssetsWithOnceCategory.get(0);
        assertThat(entity.getCategory()).isEqualTo(category);
    }

    private void initDataBase() {
        AssetEntity entity1 = new AssetEntityBuilder()
                .withAmount(new BigDecimal(1))
                .withIncomeDate(Instant.now())
                .withCategory(AssetCategory.OTHER)
                .build();

        AssetEntity entity2 = new AssetEntityBuilder()
                .withAmount(new BigDecimal(2))
                .withIncomeDate(Instant.now())
                .withCategory(AssetCategory.OTHER)
                .build();

        AssetEntity entity3 = new AssetEntityBuilder()
                .withAmount(new BigDecimal(3))
                .withIncomeDate(Instant.now())
                .withCategory(AssetCategory.SALARY)
                .build();

        assetsRepository.saveAll(asList(entity1, entity2, entity3));

    }
}
