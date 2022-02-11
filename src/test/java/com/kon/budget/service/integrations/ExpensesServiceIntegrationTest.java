package com.kon.budget.service.integrations;

import com.kon.budget.enums.ExpensesCategory;
import com.kon.budget.enums.FilterExceptionErrorMessages;
import com.kon.budget.enums.FilterParameterCalendarEnum;
import com.kon.budget.enums.MonthsEnum;
import com.kon.budget.exception.MissingExpensesFilterException;
import com.kon.budget.service.dtos.ExpensesDto;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExpensesServiceIntegrationTest extends IntegrationTestsData{

    @Test
    void shouldSaveOneExpenseInDatabase() {
        //given
        initDatabaseWithUser();
        var dto =  ExpensesDto.builder()
                .amount(BigDecimal.ONE)
                .build();
        //when
        expensesService.setExpenses(dto);
        //then
        var entitiesInDatabase = expensesRepository.findAll();
        assertThat(entitiesInDatabase).hasSize(1);
        assertThat(entitiesInDatabase.get(0).getAmount()).isEqualTo(BigDecimal.ONE);
    }

    @Test
    void shouldDeleteExpensesFromDatabase() {
        //given
        var user =initDatabaseWithUser();
        var expensesId = initExpensesInDatabase(user);
        var dto = ExpensesDto.builder()
                .amount(BigDecimal.ONE)
                .id(expensesId)
                .build();

        var entitiesInDatabase = expensesRepository.findAll();
        assertThat(entitiesInDatabase).hasSize(1);
        //when
        expensesService.deleteExpenses(dto);
        //then
        var entitiesInDatabaseAfterDelete = expensesRepository.findAll();
        assertThat(entitiesInDatabaseAfterDelete).isEmpty();
    }

    @Test
    void shouldUpdateExpenseInDatabase() {
        //given
        var user =initDatabaseWithUser();
        var expensesId = initExpensesInDatabase(user);
        var dto = ExpensesDto.builder()
                .amount(BigDecimal.TEN)
                .category(ExpensesCategory.FUN)
                .id(expensesId)
                .build();

        var entityInDatabase = expensesRepository.findById(expensesId);
        var entity = entityInDatabase.get();
        assertThat(entity.getAmount()).isEqualTo(BigDecimal.ONE);
        assertThat(entity.getCategory()).isNull();
        //when
        expensesService.updateExpenses(dto);
        //then
        var entityInDatabaseAfterUpdate = expensesRepository.findById(expensesId);
        var entityAfterUpdate = entityInDatabase.get();
        assertThat(entityAfterUpdate.getAmount()).isEqualTo(BigDecimal.TEN);
        assertThat(entityAfterUpdate.getCategory()).isEqualTo(ExpensesCategory.FUN);
    }

    @Test
    void shouldReturnAllExpensesSavedInDatabase() {
        //given
        var user =initDatabaseWithUser();
        initExpensesInDatabase(user);
        initExpensesInDatabase(user);
        //when
        var result = expensesService.getAllExpenses();
        //then
        assertThat(result).hasSize(2);
    }

    @Test
    void shouldReturnAllExpenseSavedInDatabaseFilteredByDate() {
        //given
        String fromDate = "2022-01-20";
        String toDate = "2022-01-25";
        String outOfRangeDate = "2022-01-27";
        String inRangeDate = "2022-01-21";

        var user = initDatabaseWithUser();
        initExpensesInDatabase(user, toDate);
        initExpensesInDatabase(user, fromDate);
        initExpensesInDatabase(user, outOfRangeDate);
        initExpensesInDatabase(user, inRangeDate);
        Map<String, String> filter = new HashMap<>()
        {{
            put(FilterParameterCalendarEnum.FROM_DATE.getKey(), fromDate);
            put(FilterParameterCalendarEnum.TO_DATE.getKey(), toDate);
        }};

        //when
        var result = expensesService.getFilteredExpenses(filter);
        //then
        assertThat(result).hasSize(3);
        var dateAsString = result.stream()
                .map(dto -> dto.getPurchaseData().toString().substring(0, fromDate.length()))
                .collect(Collectors.toSet());
        assertThat(dateAsString)
                .contains(fromDate, toDate, inRangeDate)
                .doesNotContain(outOfRangeDate);
    }

    @ParameterizedTest(name =  "{0}")
    @MethodSource
    void shouldThrowExceptionWhenOneOfTheFiltersIsMissing(String testName, ParameterTestData testData) {
        //given
        initDatabaseByMainUser();
        //when
        var result = assertThrows(MissingExpensesFilterException.class,
                () -> expensesService.getFilteredExpenses(testData.filter));
        //then
        assertThat(result.getMessage())
                .isEqualTo(FilterExceptionErrorMessages.MISSING_EXPENSES_FILTER_KEY.getMessage(testData.missingKey.getKey()));
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

    @Test
    void shouldReturnAllExpenseSavedInDatabaseFilteredBMonthAndYear() {
        //given
        String fromDate = "2022-01-20";
        String toDate = "2022-01-25";
        String outOfRangeDate = "2022-02-27";
        String inRangeDate = "2022-01-21";

        var user = initDatabaseWithUser();
        initExpensesInDatabase(user, toDate);
        initExpensesInDatabase(user, fromDate);
        initExpensesInDatabase(user, outOfRangeDate);
        initExpensesInDatabase(user, inRangeDate);
        Map<String, String> filter = new HashMap<>()
        {{
            put(FilterParameterCalendarEnum.MONTH.getKey(), MonthsEnum.JANUARY.name());
            put(FilterParameterCalendarEnum.YEAR.getKey(), "2022");
        }};

        //when
        var result = expensesService.getFilteredExpenses(filter);
        //then
        assertThat(result).hasSize(3);
        var dateAsString = result.stream()
                .map(dto -> dto.getPurchaseData().toString().substring(0, fromDate.length()))
                .collect(Collectors.toSet());
        assertThat(dateAsString)
                .contains(fromDate, toDate, inRangeDate)
                .doesNotContain(outOfRangeDate);
    }
}
