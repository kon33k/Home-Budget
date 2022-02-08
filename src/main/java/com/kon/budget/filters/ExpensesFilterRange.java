package com.kon.budget.filters;

import com.kon.budget.repository.ExpensesRepository;
import com.kon.budget.repository.entities.ExpensesEntity;
import com.kon.budget.repository.entities.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component("for expenses range")
@AllArgsConstructor
class ExpensesFilterRange extends FilterRangeAbstract<ExpensesEntity> {

    private final ExpensesRepository expensesRepository;

    @Override
    protected List<ExpensesEntity> getAllEntityBetweenDate(UserEntity user,
                                                           LocalDateTime fromDate,
                                                           LocalDateTime toDate,
                                                           String category) {
        return expensesRepository.findAllByBetweenDate(user, fromDate, toDate);
    }
}
