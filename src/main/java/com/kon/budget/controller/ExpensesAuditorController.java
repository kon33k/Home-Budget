package com.kon.budget.controller;

import com.kon.budget.enums.MonthsEnum;
import com.kon.budget.service.auditors.ExpensesAuditorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auditor")
public class ExpensesAuditorController {

    private final ExpensesAuditorService expensesAuditorService;

    @GetMapping("/expenses/month/{month}/year/{year}")
    public BigDecimal getExpensesAudit(
            @PathVariable("month") String month,
            @PathVariable("year") String year
    ) {
        return expensesAuditorService.getAudit(MonthsEnum.valueOf(month), year);
    }
}
