package com.kon.budget.filters;

import com.kon.budget.repository.AssetsRepository;
import com.kon.budget.repository.entities.AssetEntity;
import com.kon.budget.repository.entities.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class AssetsFilterRange extends FilterRangeAbstract {

    private final AssetsRepository assetsRepository;

    @Override
    protected List<AssetEntity> getAllEntityBetweenDate(UserEntity user,
                                                        LocalDateTime fromDate,
                                                        LocalDateTime toDate) {
        return assetsRepository.findAllByBetweenDate(user, fromDate, toDate);
    }

    @Override
    protected String getFilterName() {
        return "AssetsFilter";
    }
}
