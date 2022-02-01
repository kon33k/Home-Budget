package com.kon.budget.filters;

import com.kon.budget.repository.ExpensesRepository;
import com.kon.budget.repository.entities.ExpensesEntity;
import com.kon.budget.repository.entities.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class ExpensesFilterRange extends FilterRangeAbstract{

    private final ExpensesRepository expensesRepository;

    @Override
    protected List<ExpensesEntity> getAllEntityBetweenDate(UserEntity user, LocalDateTime fromDate, LocalDateTime toDate) {
        return expensesRepository.findAllByBetweenDate(user, fromDate, toDate);
    }

    @Override
    protected String getFilterName() {
        return "ExpensesFilter";
    }
}
