package com.kon.budget.controller;

import com.kon.budget.service.ExpensesService;
import com.kon.budget.service.dtos.ExpensesDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/expenses")
@AllArgsConstructor
public class ExpensesController {

    private final ExpensesService expensesService;

    @GetMapping
    public List<ExpensesDto> getAllExpenses() {
        return expensesService.getAllExpenses();
    }

    @PostMapping
    public void setExpenses(@RequestBody ExpensesDto expensesDto) {
        expensesService.setExpenses(expensesDto);
    }

    @PutMapping
    public void updateExpense(@RequestBody ExpensesDto expensesDto) {
        expensesService.updateExpenses(expensesDto);
    }

    @DeleteMapping
    public void deleteExpense(@RequestBody ExpensesDto expensesDto) {
        expensesService.deleteExpenses(expensesDto);
    }

    @GetMapping("/filter")
    public  List<ExpensesDto> getFilteredExpenses(@RequestParam Map<String, String> filter) {
        return expensesService.getFilteredExpenses(filter);
    }
}
