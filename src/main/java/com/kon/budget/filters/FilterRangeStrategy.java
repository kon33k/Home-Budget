package com.kon.budget.filters;

import com.kon.budget.enums.FilterSpecification;
import com.kon.budget.repository.entities.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class FilterRangeStrategy<T> {


    private final Map<String, FilterRangeAbstract> allFilterRange;


    public List<T> getFilteredDataFromSpecification(UserEntity user,
                                                 Map<String, String> filters,
                                                 FilterSpecification filterSpecification)  {
        return allFilterRange.get(filterSpecification.getForRange())
                .getAllByFilter(user, filters, filterSpecification);

    }
}
