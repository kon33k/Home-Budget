package com.kon.budget.service.upload;

import com.kon.budget.enums.AssetCategory;
import com.kon.budget.service.dtos.AssetDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
class ParseAssetsService {

    /**
    * zamienia stringa z pliku na dto
     */
    public List<AssetDto> mapToDto(List<String> bufferedReader) {
        return bufferedReader.stream()
                .map(data -> data.split(";"))
                .map(array -> AssetDto.builder()
                        .amount(new BigDecimal(array[0]))
                        .category(AssetCategory.valueOf(array[1].toUpperCase()))
                        .incomeDate(LocalDateTime.parse(array[2] + "T00:00:00.000000001"))
                        .description(array[3])
                        .build())
                .collect(Collectors.toList());
    }
}
