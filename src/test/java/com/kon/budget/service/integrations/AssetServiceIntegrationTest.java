package com.kon.budget.service.integrations;

import com.kon.budget.builder.AssetDtoBuilder;
import com.kon.budget.enums.AssetCategory;
import com.kon.budget.repository.entities.UserEntity;
import com.kon.budget.service.dtos.AssetDto;
import org.assertj.core.util.Streams;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class AssetServiceIntegrationTest extends IntegrationTestsData{

    @Test
    void shouldReturnListWithThreeElements() {
        // given
        initDataBaseByMainMockUserAndHisAssets();
        initDataBaseBySecondMockUserAndHisAssets();
        // when
        var allAssetsInDB = service.getAllAssets();
        // then
        assertThat(allAssetsInDB).hasSize(3);
    }
    @Test
    void shouldAddAssetToDB() {
        // given
        initMainMockUserInToDatabase();
        AssetDto dto = new AssetDtoBuilder()
                .withAmount(new BigDecimal(11))
                .withIncomeDate(Instant.now())
                .withCategory(AssetCategory.RENT)
                .build();
        // when
        service.setAsset(dto);
        // then
        var allAssetInDB = assetsRepository.findAll();
        assertThat(allAssetInDB).hasSize(1);
        var entity = allAssetInDB.get(0);
        assertThat(entity.getCategory()).isEqualTo(dto.getCategory());
        assertThat(entity.getAmount()).isEqualTo(dto.getAmount());
        assertThat(entity.getIncomeDate()).isEqualTo(dto.getIncomeDate());
    }
    @Test
    void shouldReturnListOnlyWithOneCategory() {
        // given
        initDataBaseByMainMockUserAndHisAssets();
        var category = AssetCategory.OTHER;
        // when
        var allAssetsWithOneCategory = service.getAssetsByCategory(category);
        // then
        assertThat(allAssetsWithOneCategory).hasSize(1);
        var entity = allAssetsWithOneCategory.get(0);
        assertThat(entity.getCategory()).isEqualTo(category);
    }
    @Test
    void shouldDeleteAllAssetsOfChosenUser() {
        //given
        initDataBaseByMainMockUserAndHisAssets();
        initDataBaseBySecondMockUserAndHisAssets();
        int numberOfAllAssets = 6;
        int numberOfRemainingAssets = 3;

        var allUsers = userRepository.findAll();
        var userToDeleteAssets = Streams.stream(allUsers).findFirst();
        UserEntity userEntity = userToDeleteAssets.get();
        var userToLeaveAssets = Streams.stream(allUsers)
                .filter(entity -> !entity.equals(userEntity))
                .findFirst().get();

        var assetsOfTwoUsers = assetsRepository.findAll();
        assertThat(assetsOfTwoUsers).hasSize(numberOfAllAssets);
        //when
        service.deleteAllAssetsByUser(userEntity);
        //then
        var assetsAfterDelete = assetsRepository.findAll();
        assertThat(assetsAfterDelete).hasSize(numberOfRemainingAssets);

        var assetsUserId = assetsAfterDelete.stream()
                .map(assetEntity -> assetEntity.getUser())
                .map(ue -> ue.getId())
                .distinct()
                .collect(Collectors.toList());
        assertThat(assetsUserId).hasSize(1)
                .containsExactly(userToLeaveAssets.getId());
    }
}