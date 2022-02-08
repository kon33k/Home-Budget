package com.kon.budget.service;

import com.kon.budget.enums.FilterSpecification;
import com.kon.budget.filters.FilterRangeStrategy;
import com.kon.budget.mapper.ExpensesMapper;
import com.kon.budget.repository.ExpensesRepository;
import com.kon.budget.repository.entities.ExpensesEntity;
import com.kon.budget.repository.entities.UserEntity;
import com.kon.budget.service.dtos.ExpensesDto;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ExpensesService {

    public static final Logger LOGGER = LoggerFactory.getLogger(ExpensesService.class.getName());

    private final ExpensesRepository expensesRepository;
    private final ExpensesMapper expensesMapper;
    private final UserLogInfoService userLogInfoService;
    private final FilterRangeStrategy<ExpensesEntity> filterRangeStrategy;

    /**
    * pobranie wszystkich encji expeneses z bazy danych
     */

    public List<ExpensesDto> getAllExpenses() {
        LOGGER.debug("Get all expenses");
        var user = userLogInfoService.getLoggedUserEntity();
        var allExpenses = expensesRepository.findAllByUser(user);
        return expensesMapper.fromEntitiesToDtos(allExpenses);
    }

    /**
    * dodanie encji expeneses do bazy dancych
     */

    public void setExpenses(ExpensesDto dto) {
        LOGGER.info("Set Expense");
        LOGGER.debug("ExpenseDto {}", dto);
        var user = userLogInfoService.getLoggedUserEntity();
        var entity = expensesMapper.fromDtoToEntity(dto, user);

        expensesRepository.save(entity);
        LOGGER.info("ExpenseSaved");
    }

    /**
    * usuniecie encji Expeneses w bazie danych
     */

    public void deleteExpenses(ExpensesDto dto) {
        LOGGER.info("Delete expense");
        LOGGER.debug("ExpenseDto {}", dto);
        UserEntity user = userLogInfoService.getLoggedUserEntity();
        var entity = expensesMapper.fromDtoToEntity(dto, user);

        expensesRepository.delete(entity);
        LOGGER.info("Expense deleted");
    }

    /**
    * aktualizacja encji expeneses w bazie dancyh
    */

    @Transactional
    public void updateExpenses(ExpensesDto dto) {
        LOGGER.info("Update Expense");
        LOGGER.debug("ExpnseDto {} ", dto);
        var entity = expensesRepository.findById(dto.getId());
        entity.ifPresent(expensesEntity ->
                updateExpenses(expensesEntity, dto));
        LOGGER.info("Expense Updated");
    }

    /**
    * sprawdza pola przed aktualizacja czy nie jest
    * puste i czy nie ma takiej samej wartości w ecnji
    */

    private void updateExpenses(ExpensesEntity entity, ExpensesDto dto) {
        if (Objects.nonNull(dto.getPurchaseData())
        && !dto.getPurchaseData().equals(entity.getPurchaseDate())) {
            entity.setPurchaseDate(dto.getPurchaseData());
        }
        if (Objects.nonNull(dto.getAmount())
        && !dto.getAmount().equals(entity.getAmount())) {
            entity.setAmount(dto.getAmount());
        }
        if (Objects.nonNull(dto.getCategory())
        && !dto.getCategory().equals(entity.getCategory())) {
            entity.setCategory(dto.getCategory());
        }
    }

    /**
     * pobranie wszystkich encji z bazy danych które spełniaja wymagania filtrów
     * od daty do daty lub miesciac i rok
     */

    public List<ExpensesDto> getFilteredExpenses(Map<String, String> filter) {
        var user = userLogInfoService.getLoggedUserEntity();
        FilterSpecification specification = FilterSpecification.FOR_EXPENSES;

        return filterRangeStrategy.getFilteredDataFromSpecification(user, filter, specification)
                .stream().map(expensesMapper::fromEntityToDtos)
                .collect(Collectors.toList());
    }

}
