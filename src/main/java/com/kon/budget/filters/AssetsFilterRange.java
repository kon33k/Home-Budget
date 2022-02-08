package com.kon.budget.filters;

import com.kon.budget.enums.AssetCategory;
import com.kon.budget.repository.AssetsRepository;
import com.kon.budget.repository.entities.AssetEntity;
import com.kon.budget.repository.entities.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component("for assets range")
@AllArgsConstructor
class AssetsFilterRange extends FilterRangeAbstract<AssetEntity> {

    private final AssetsRepository assetsRepository;

    @Override
    protected List<AssetEntity> getAllEntityBetweenDate(UserEntity user,
                                                        LocalDateTime fromDate,
                                                        LocalDateTime toDate,
                                                        String category) {
        return assetsRepository.findAllByBetweenDate(user, fromDate, toDate, mapStrongToEnum(category));
    }

    private List<AssetCategory> mapStrongToEnum(String category) {
        if(Objects.isNull(category)) {
            return Arrays.asList(AssetCategory.values());
        }
        return Arrays.asList(AssetCategory.valueOf(category.toUpperCase()));
    }
}
