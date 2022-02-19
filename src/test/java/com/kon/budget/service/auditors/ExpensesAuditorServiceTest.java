package com.kon.budget.service.auditors;

import com.kon.budget.enums.MonthsEnum;
import com.kon.budget.service.AssetsService;
import com.kon.budget.service.ExpensesService;
import com.kon.budget.service.dtos.AssetDto;
import com.kon.budget.service.dtos.ExpensesDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.BIG_DECIMAL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpensesAuditorServiceTest {

    @Mock
    private AssetsService assetsService;
    @Mock
    private ExpensesService expensesService;

    private ExpensesAuditorService expensesAuditorService;

    @BeforeEach
    public void setup() {
        expensesAuditorService = new ExpensesAuditorService(
                assetsService, expensesService
        );
    }

    @Test
    void shouldReturnInformationAboutExceedExpenses() {
        //given
        when(assetsService.getFilteredAssets(anyMap())).thenReturn(getAssetsList());
        when(expensesService.getFilteredExpenses(anyMap())).thenReturn(getExpensesList());
        //when
        var result = expensesAuditorService.getAudit(MonthsEnum.JANUARY, "2022");
        //then
        assertThat(result).isEqualTo(BigDecimal.ONE);
    }

    private List<ExpensesDto> getExpensesList() {
        return asList(
                ExpensesDto.builder()
                .amount(new BigDecimal(9))
                .build()
        );
    }

    private List<AssetDto> getAssetsList() {
        return asList(
                AssetDto.builder()
                .amount(BigDecimal.TEN)
                .build()
        );
    }
}