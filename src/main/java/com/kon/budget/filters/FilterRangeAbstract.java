package com.kon.budget.filters;

import com.kon.budget.enums.FilterParameterCalendarEnum;
import com.kon.budget.enums.FilterSpecification;
import com.kon.budget.enums.MonthsEnum;
import com.kon.budget.repository.entities.UserEntity;
import com.kon.budget.validator.filters.FilterStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

abstract class FilterRangeAbstract<T> {

    private static final String DATE_SUFFIX = "T00:00:00.000000001";

    @Autowired
    private FilterStrategy filterStrategy;

    /**
    * pobiera list encji assets / expesnes, wywołuje i sprawdza czy klucze w filtrze są datami lub miesiac i rok
     */
    public List<T> getAllByFilter(UserEntity user,
                                  Map<String, String> filter,
                                  FilterSpecification filterSpecification) {

        filterStrategy.checkFilterForSpecification(filter, filterSpecification);

        if(isFilterForFromToDate(filter)) {
            var fromDate = filter.get(FilterParameterCalendarEnum.FROM_DATE.getKey());
            var toDate = filter.get(FilterParameterCalendarEnum.TO_DATE.getKey());

            return getAllEntityBetweenDate(user,
                                            parseDateToLocalDateTime(fromDate),
                                            parseDateToLocalDateTime(toDate),
                                            filter.get(FilterParameterCalendarEnum.CATEGORY.getKey()));

        } else if (isFilterForMonthAndYear(filter)){
            MonthsEnum month = MonthsEnum.valueOf(filter.get(FilterParameterCalendarEnum.MONTH.getKey()).toUpperCase());
            String year = filter.get(FilterParameterCalendarEnum.YEAR.getKey());
            return getAllExpensesFromMonthAndYear(user,
                                                  month,
                                                  year,
                                                  filter.get(FilterParameterCalendarEnum.CATEGORY.getKey()));
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

    private List<T> getAllExpensesFromMonthAndYear(UserEntity user,
                                                   MonthsEnum month,
                                                   String year,
                                                   String category) {
        String from = month.getFirstDayForYear(year);
        String to = month.getLastDayForYear(year);

        return getAllEntityBetweenDate(user, parseDateToLocalDateTime(from), parseDateToLocalDateTime(to), category);
    }

    /**
    *
     */
    private LocalDateTime parseDateToLocalDateTime(String date) {
        return LocalDateTime.parse(date + DATE_SUFFIX);
    }

    /**
    * wywołuje odpowiednie repozyturium i pobiera encje w zależności od filtra jaki jest przekazywany
     * @param user
     * @param fromDate
     * @param toDate
     * @param category
     */
    protected abstract List<T> getAllEntityBetweenDate(UserEntity user, LocalDateTime fromDate, LocalDateTime toDate, String category);

}
