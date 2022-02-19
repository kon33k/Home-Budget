package com.kon.budget.service.auditors;

import com.kon.budget.enums.FilterParameterCalendarEnum;
import com.kon.budget.enums.MonthsEnum;
import com.kon.budget.service.AssetsService;
import com.kon.budget.service.ExpensesService;
import com.kon.budget.service.dtos.AssetDto;
import com.kon.budget.service.dtos.ExpensesDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ExpensesAuditorService {

    /**
    * serwis odpowiedzialny za licznie bilansu konta
     */

    private final AssetsService assetsService;
    private final ExpensesService expensesService;

    public BigDecimal getAudit(MonthsEnum months, String year) {

        var assetsInMonth = getAssetsInMonth(months, year);
        var expensesInMonth = getExpensesInMonth(months, year);

        var assetsSum = assetsInMonth.stream()
                .map(AssetDto::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var expensesSum = expensesInMonth.stream()
                .map(ExpensesDto::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return assetsSum.subtract(expensesSum);
    }

    private List<AssetDto> getAssetsInMonth(MonthsEnum months, String year) {
        var filters = getFilters(months, year);
        return assetsService.getFilteredAssets(filters);
    }

    private List<ExpensesDto> getExpensesInMonth(MonthsEnum months, String year) {
        var filters = getFilters(months, year);
        return expensesService.getFilteredExpenses(filters);
    }

    private Map<String, String> getFilters(MonthsEnum months, String year) {
        var fromDate = months.getFirstDayForYear(year);
        var toDate = months.getLastDayForYear(year);

        return new HashMap<>() {{
            put(FilterParameterCalendarEnum.FROM_DATE.getKey(), fromDate);
            put(FilterParameterCalendarEnum.TO_DATE.getKey(), toDate);
        }};
    }
}
