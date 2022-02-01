package com.kon.budget.filters;

import com.kon.budget.enums.FilterParameterCalendarEnum;
import com.kon.budget.enums.MonthsEnum;
import com.kon.budget.repository.entities.UserEntity;
import com.kon.budget.validator.AssetsFilterParametersValidator;
import com.kon.budget.validator.ExpensesFilterParametersValidator;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class FilterRangeAbstract<T> {

    private static final String DATE_SUFFIX = "T00:00:00.000000001";

    @Autowired
    private ExpensesFilterParametersValidator expensesFilterParametersValidator;

    @Autowired
    private AssetsFilterParametersValidator assetsFilterParametersValidator;

    /**
    * pobiera list encji assets / expesnes, wywołuje i sprawdza czy klucze w filtrze są datami lub miesiac i rok
     */
    public List<T> getAllByFilter( UserEntity user ,Map<String, String> filter) {
        if ("ExpensesFilter".equals(getFilterName())) {
            expensesFilterParametersValidator.assertFilter(filter);
        }
        if ("AssetsFilter".equals(getFilterName())) {
            assetsFilterParametersValidator.assertFilter(filter);
        }
        if(isFilterForFromToDate(filter)) {
            var fromDate = filter.get(FilterParameterCalendarEnum.FROM_DATE.getKey());
            var toDate = filter.get(FilterParameterCalendarEnum.TO_DATE.getKey());
            return getAllEntityBetweenDate(user , parseDateToLocalDateTime(fromDate), parseDateToLocalDateTime(toDate));
        } else if (isFilterForMonthAndYear(filter)){
            MonthsEnum month = MonthsEnum.valueOf(filter.get(FilterParameterCalendarEnum.MONTH.getKey()).toUpperCase());
            String year = filter.get(FilterParameterCalendarEnum.YEAR.getKey());
            return getAllExpensesFromMonthAndYear(user, month, year);
        }
        return Collections.emptyList();
    }

    /**
     * SPRAWDZA CZY W MAPIE FILTRÓW JEST YEAR / MONTH
     */
    private boolean isFilterForMonthAndYear(Map<String, String> filter) {
        return filter.containsKey(FilterParameterCalendarEnum.YEAR.getKey())
                && filter.containsKey(FilterParameterCalendarEnum.MONTH.getKey());
    }

    /**
     * SPRAWDZA CZY W MAPIE FILTRÓW JEST FROM_DATE / TO_DATE
     */
    private boolean isFilterForFromToDate(Map<String, String> filter) {
        return filter.containsKey(FilterParameterCalendarEnum.FROM_DATE.getKey())
                && filter.containsKey(FilterParameterCalendarEnum.TO_DATE.getKey());
    }

    private List<T> getAllExpensesFromMonthAndYear(UserEntity user, MonthsEnum month, String year) {
        String from = month.getFirstDayForYear(year);
        String to = month.getLastDayForYear(year);

        return getAllEntityBetweenDate(user, parseDateToLocalDateTime(from), parseDateToLocalDateTime(to));
    }

    private LocalDateTime parseDateToLocalDateTime(String date) {
        return LocalDateTime.parse(date + DATE_SUFFIX);
    }

    /**
    * wywołuje odpowiednie repozyturium i pobiera encje w zależności od filtra jaki jest przekazywany
     * @param user
     * @param fromDate
     * @param toDate
     */
    protected abstract List<T> getAllEntityBetweenDate(UserEntity user, LocalDateTime fromDate, LocalDateTime toDate);

    protected abstract String getFilterName();
}
