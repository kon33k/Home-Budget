package com.kon.budget.service.upload;

import com.kon.budget.enums.AssetCategory;
import com.kon.budget.enums.ExpensesCategory;
import com.kon.budget.service.dtos.AssetDto;
import com.kon.budget.service.dtos.ExpensesDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ParseExpensesService {

    /**
     * zamienia stringa z pliku na dto
     */
    public List<ExpensesDto> mapToDto(List<String> bufferedReader) {
        return bufferedReader.stream()
                .map(data -> data.split(";"))
                .map(array -> ExpensesDto.builder()
                        .amount(new BigDecimal(array[0]))
                        .category(ExpensesCategory.valueOf(array[1].toUpperCase()))
                        .purchaseData(LocalDateTime.parse(array[2] + "T00:00:00.000000001"))
                        .build())
                .collect(Collectors.toList());
    }
}
