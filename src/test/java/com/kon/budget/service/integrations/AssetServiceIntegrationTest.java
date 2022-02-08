package com.kon.budget.service.integrations;

import com.kon.budget.builder.AssetDtoBuilder;
import com.kon.budget.enums.AssetCategory;
import com.kon.budget.enums.FilterExceptionErrorMessages;
import com.kon.budget.enums.FilterParameterCalendarEnum;
import com.kon.budget.enums.MonthsEnum;
import com.kon.budget.exception.MissingAssetsFilterException;
import com.kon.budget.repository.entities.UserEntity;
import com.kon.budget.service.dtos.AssetDto;
import lombok.AllArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.assertj.core.util.Streams;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AssetServiceIntegrationTest extends IntegrationTestsData{

    @Test
    void shouldReturnListWithThreeElements() {
        // given
        initDataBaseByMainMockUserAndHisAssets();
        initDataBaseBySecondMockUserAndHisAssets();
        // when
        var allAssetsInDB = assetsService.getAllAssets();
        // then
        assertThat(allAssetsInDB).hasSize(3);
    }
    @Test
    void shouldAddAssetToDB() {
        // given
        initMainMockUserInToDatabase();
        AssetDto dto = new AssetDtoBuilder()
                .withAmount(new BigDecimal(11))
                .withIncomeDate(LocalDateTime.now())
                .withCategory(AssetCategory.RENT)
                .build();
        // when
        assetsService.setAsset(dto);
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
        var allAssetsWithOneCategory = assetsService.getAssetsByCategory(category);
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
        assetsService.deleteAllAssetsByUser(userEntity);
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

    @Test
    void shouldReturnAllAssetsByFilterFromTo() {
        //given
        String fromDate = "2022-01-20";
        String toDate = "2022-01-28";
        String middleDate = "2022-01-24";
        String outOfRangeDate = "2022-01-30";

        var user = initDatabaseWithUser();
        initDatabaseWithUserAssets(user, fromDate);
        initDatabaseWithUserAssets(user, toDate);
        initDatabaseWithUserAssets(user, middleDate);
        initDatabaseWithUserAssets(user, outOfRangeDate);

        Map<String, String> filter = new HashMap<>() {{
            put(FilterParameterCalendarEnum.FROM_DATE.getKey(), fromDate);
            put(FilterParameterCalendarEnum.TO_DATE.getKey(), toDate);
        }};

        //when
        var result = assetsService.getFilteredAssets(filter);
        //then
        assertThat(result).hasSize(3);
        var dateAsString = result.stream()
                .map(dto -> dto.getIncomeDate().toString().substring(0, fromDate.length()))
                .collect(Collectors.toList());
        assertThat(dateAsString).hasSize(3)
                .contains(fromDate, toDate, middleDate)
                .doesNotContain(outOfRangeDate);
    }

    @Test
    void shouldReturnAllAssetsByFilterFromToAndCategory() {
        //given
        var lookingCategory = AssetCategory.RENT;
        var notLookingCategory = AssetCategory.SALARY;

        String fromDate = "2022-01-20";
        String toDate = "2022-01-28";
        String middleDate = "2022-01-24";
        String middleDate2 = "2022-01-23";
        String outOfRangeDate = "2022-01-30";
        String outOfRangeDate2 = "2022-01-31";

        var user = initDatabaseWithUser();
        initDatabaseWithUserAssets(user, fromDate, lookingCategory);
        initDatabaseWithUserAssets(user, toDate, lookingCategory);
        initDatabaseWithUserAssets(user, toDate, notLookingCategory);
        initDatabaseWithUserAssets(user, middleDate, lookingCategory);
        initDatabaseWithUserAssets(user, middleDate2, notLookingCategory);
        initDatabaseWithUserAssets(user, outOfRangeDate, lookingCategory);
        initDatabaseWithUserAssets(user, outOfRangeDate2, notLookingCategory);

        Map<String, String> filter = new HashMap<>() {{
            put(FilterParameterCalendarEnum.FROM_DATE.getKey(), fromDate);
            put(FilterParameterCalendarEnum.TO_DATE.getKey(), toDate);
            put(FilterParameterCalendarEnum.CATEGORY.getKey() , lookingCategory.name());
        }};

        //when
        var result = assetsService.getFilteredAssets(filter);
        //then
        assertThat(result).hasSize(3);
        var dateAsString = result.stream()
                .map(dto -> dto.getIncomeDate().toString().substring(0, fromDate.length()))
                .collect(Collectors.toList());
        assertThat(dateAsString).hasSize(3)
                .contains(fromDate, toDate, middleDate)
                .doesNotContain(outOfRangeDate);
    }

    @Test
    void shouldReturnAllAssetsByFilterByMonthAndYear() {
        //given
        String fromDate = "2022-01-20";
        String toDate = "2022-01-28";
        String middleDate = "2022-01-24";
        String outOfRangeDate = "2022-02-27";

        var user = initDatabaseWithUser();
        initDatabaseWithUserAssets(user, fromDate);
        initDatabaseWithUserAssets(user, toDate);
        initDatabaseWithUserAssets(user, middleDate);
        initDatabaseWithUserAssets(user, outOfRangeDate);

        Map<String, String> filter = new HashMap<>() {{
            put(FilterParameterCalendarEnum.MONTH.getKey(), MonthsEnum.JANUARY.name());
            put(FilterParameterCalendarEnum.YEAR.getKey(), "2022");
        }};

        //when
        var result = assetsService.getFilteredAssets(filter);
        //then
        assertThat(result).hasSize(3);
        var dateAsString = result.stream()
                .map(dto -> dto.getIncomeDate().toString().substring(0, fromDate.length()))
                .collect(Collectors.toList());
        assertThat(dateAsString).hasSize(3)
                .contains(fromDate, toDate, middleDate)
                .doesNotContain(outOfRangeDate);
    }



    @ParameterizedTest(name =  "{0}")
    @MethodSource
    void shouldThrowExceptionWhenOneOfTheFiltersIsMissing(String testName, AssetServiceIntegrationTest.ParameterTestData testData) {
        //given
        initDatabaseByMainUser();
        //when
        var result = assertThrows(MissingAssetsFilterException.class,
                () -> assetsService.getFilteredAssets(testData.filter));
        //then
        AssertionsForClassTypes.assertThat(result.getMessage())
                .isEqualTo(FilterExceptionErrorMessages.MISSING_ASSETS_FILTER_KEY.getMessage(testData.missingKey.getKey()));
    }

    private static Stream<Arguments> shouldThrowExceptionWhenOneOfTheFiltersIsMissing() {
        return Stream.of(
                Arguments.of("test for missing " + FilterParameterCalendarEnum.FROM_DATE.getKey(),
                        new ParameterTestData(
                                new HashMap<>() {{
                                    put(FilterParameterCalendarEnum.TO_DATE.getKey(), "2022-02-20");
                                }},
                                FilterParameterCalendarEnum.FROM_DATE
                        )
                ),

                Arguments.of("test for missing " + FilterParameterCalendarEnum.TO_DATE.getKey(),
                        new ParameterTestData(
                                new HashMap<>() {{
                                    put(FilterParameterCalendarEnum.FROM_DATE.getKey(), "2022-02-20");
                                }},
                                FilterParameterCalendarEnum.TO_DATE
                        )
                ),

                Arguments.of("test for missing " + FilterParameterCalendarEnum.MONTH.getKey(),
                        new ParameterTestData(
                                new HashMap<>() {{
                                    put(FilterParameterCalendarEnum.MONTH.getKey(), "january");
                                }},
                                FilterParameterCalendarEnum.YEAR
                        )
                ),

                Arguments.of("test for missing " + FilterParameterCalendarEnum.YEAR.getKey(),
                        new ParameterTestData(
                                new HashMap<>() {{
                                    put(FilterParameterCalendarEnum.YEAR.getKey(), "2022");
                                }},
                                FilterParameterCalendarEnum.MONTH
                        )
                )
        );
    }

    @AllArgsConstructor
    private static class ParameterTestData {
        public Map<String, String> filter;
        public FilterParameterCalendarEnum missingKey;
    }

}