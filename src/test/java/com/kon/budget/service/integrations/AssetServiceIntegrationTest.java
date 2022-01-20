package com.kon.budget.service.integrations;

import com.kon.budget.builder.AssetDtoBuilder;
import com.kon.budget.builder.AssetEntityBuilder;
import com.kon.budget.enums.AssetCategory;
import com.kon.budget.repository.AssetsRepository;
import com.kon.budget.repository.UserRepository;
import com.kon.budget.repository.entities.AssetEntity;
import com.kon.budget.repository.entities.UserEntity;
import com.kon.budget.service.AssetsService;
import com.kon.budget.service.dtos.AssetDto;
import org.assertj.core.util.Streams;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@Transactional
@WithMockUser(username = "userTest", password = "testUser")
public class AssetServiceIntegrationTest {

    @Autowired
    private AssetsRepository assetsRepository;
    @Autowired
    private AssetsService service;
    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldReturnListWithThreeElements() {
        // given
        initDataBaseByDefaultMockUserAndHisAssets();
        initDataBaseBySecondMockUserAndHisAssets();
        // when
        var allAssetsInDB = service.getAllAssets();
        // then
        assertThat(allAssetsInDB).hasSize(3);
    }
    @Test
    void shouldAddAssetToDB() {
        // given
        initDefaultMockUserInToDatabase();
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
        initDataBaseByDefaultMockUserAndHisAssets();
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
        initDataBaseByDefaultMockUserAndHisAssets();
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
    private void initDataBaseByDefaultMockUserAndHisAssets() {
        UserEntity userEntity = initDefaultMockUserInToDatabase();
        AssetEntity entity1 = new AssetEntityBuilder()
                .withAmount(new BigDecimal(1))
                .withIncomeDate(Instant.now())
                .withCategory(AssetCategory.OTHER)
                .withUser(userEntity)
                .build();
        AssetEntity entity2 = new AssetEntityBuilder()
                .withAmount(new BigDecimal(3))
                .withIncomeDate(Instant.now())
                .withCategory(AssetCategory.SALARY)
                .withUser(userEntity)
                .build();
        AssetEntity entity3 = new AssetEntityBuilder()
                .withAmount(new BigDecimal(5))
                .withIncomeDate(Instant.now())
                .withCategory(AssetCategory.RENT)
                .withUser(userEntity)
                .build();
        assetsRepository.saveAll(asList(entity1, entity2, entity3));
    }

    private UserEntity initDefaultMockUserInToDatabase() {
        UserEntity user = new UserEntity();
        user.setUsername("userTest");
        user.setPassword("testUser");
        return userRepository.save(user);
    }


    private void initDataBaseBySecondMockUserAndHisAssets() {
        UserEntity userEntity = initSecondMockUserInToDatabase();
        AssetEntity entity1 = new AssetEntityBuilder()
                .withAmount(new BigDecimal(1))
                .withIncomeDate(Instant.now())
                .withCategory(AssetCategory.OTHER)
                .withUser(userEntity)
                .build();
        AssetEntity entity2 = new AssetEntityBuilder()
                .withAmount(new BigDecimal(3))
                .withIncomeDate(Instant.now())
                .withCategory(AssetCategory.SALARY)
                .withUser(userEntity)
                .build();
        AssetEntity entity3 = new AssetEntityBuilder()
                .withAmount(new BigDecimal(5))
                .withIncomeDate(Instant.now())
                .withCategory(AssetCategory.RENT)
                .withUser(userEntity)
                .build();
        assetsRepository.saveAll(asList(entity1, entity2, entity3));
    }


    private UserEntity initSecondMockUserInToDatabase() {
        UserEntity user = new UserEntity();
        user.setUsername("userTest2");
        user.setPassword("testUser2");
        return userRepository.save(user);
    }

}