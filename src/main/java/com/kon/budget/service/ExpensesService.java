package com.kon.budget.service;

import com.kon.budget.enums.ExpensesExceptionErrorMessages;
import com.kon.budget.enums.FilterExpensesParameterEnum;
import com.kon.budget.enums.MonthsEnum;
import com.kon.budget.exception.MissingExpensesFilterException;
import com.kon.budget.mapper.ExpensesMapper;
import com.kon.budget.repository.ExpensesRepository;
import com.kon.budget.repository.entities.ExpensesEntity;
import com.kon.budget.repository.entities.UserEntity;
import com.kon.budget.service.dtos.ExpensesDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ExpensesService {

    public static final Logger LOGGER = LoggerFactory.getLogger(AssetsService.class.getName());

    @Autowired
    private ExpensesRepository expensesRepository;
    @Autowired
    private ExpensesMapper expensesMapper;
    @Autowired
    private UserLogInfoService userLogInfoService;

    public List<ExpensesDto> getAllExpenses() {
        LOGGER.debug("Get all expenses");
        var user = userLogInfoService.getLoggedUserEntity();
        var allExpenses = expensesRepository.findAllByUser(user);
        return expensesMapper.fromEntitiesToDtos(allExpenses);
    }

    public void setExpenses(ExpensesDto dto) {
        LOGGER.info("Set Expense");
        LOGGER.debug("ExpenseDto {}", dto);
        var user = userLogInfoService.getLoggedUserEntity();
        var entity = expensesMapper.fromDtoToEntity(dto, user);

        expensesRepository.save(entity);
        LOGGER.info("ExpenseSaved");
    }

    public void deleteExpenses(ExpensesDto dto) {
        LOGGER.info("Delete expense");
        LOGGER.debug("ExpenseDto {}", dto);
        UserEntity user = userLogInfoService.getLoggedUserEntity();
        var entity = expensesMapper.fromDtoToEntity(dto, user);

        expensesRepository.delete(entity);
        LOGGER.info("Expense deleted");
    }

    /*
    na podstaie id z dto wyciaga encje
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


    /*
    sprawdza pola przed aktualizacja czy nie jest
     puste i czy nie ma takiej samej wartości w ecnji
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
     * filtrowanie assets i expenses po dacie
     */

    public List<ExpensesDto> getFilteredExpenses(Map<String, String> filter) {
        if(isFilterForFromToDate(filter)) {
            return getAllExpensesBetweenDate(
                    filter.get(FilterExpensesParameterEnum.FROM_DATE.getKey()),
                    filter.get(FilterExpensesParameterEnum.TO_DATE.getKey())
            );
        } else if (isFilterForMonthAndYear(filter)){
            MonthsEnum month = MonthsEnum.valueOf(filter.get(FilterExpensesParameterEnum.MONTH.getKey()).toUpperCase());
            String year = filter.get(FilterExpensesParameterEnum.YEAR.getKey());
            return getAllExpensesFromMonthAndYear(month, year);
        }
        return Collections.emptyList();
    }
    /**
    * SPRAWDZA CZY W MAPIE FILTRÓW JEST FROM_DATE / TO_DATE
     */
    private boolean isFilterForFromToDate(Map<String, String> filter) {
        if(filter.containsKey(FilterExpensesParameterEnum.FROM_DATE.getKey())
                && !filter.containsKey(FilterExpensesParameterEnum.TO_DATE.getKey())) {
            throw new MissingExpensesFilterException(
                    getMessageToException(FilterExpensesParameterEnum.TO_DATE.getKey()),
                    "01FTBQPDCDN0H07Z7RX4JKF1BE"); //random ULID
        }
        if(filter.containsKey(FilterExpensesParameterEnum.TO_DATE.getKey())
                && !filter.containsKey(FilterExpensesParameterEnum.FROM_DATE.getKey())) {
            throw new MissingExpensesFilterException(
                    getMessageToException(FilterExpensesParameterEnum.FROM_DATE.getKey()),
                    "01FTBQSXRGNQ60WWYE2ENE953W"); //random ULID
        }

        return filter.containsKey(FilterExpensesParameterEnum.FROM_DATE.getKey())
                && filter.containsKey(FilterExpensesParameterEnum.TO_DATE.getKey());
    }

    private String getMessageToException(String missingKey) {
        return ExpensesExceptionErrorMessages.MISSING_FILTER_KEY.getMessage() + missingKey;
    }

    /**
    * SPRAWDZA CZY W MAPIE FILTRÓW JEST YEAR / MONTH
     */
    private boolean isFilterForMonthAndYear(Map<String, String> filter) {
        if(filter.containsKey(FilterExpensesParameterEnum.YEAR.getKey())
                && !filter.containsKey(FilterExpensesParameterEnum.MONTH.getKey())) {
            throw new MissingExpensesFilterException(
                    getMessageToException(FilterExpensesParameterEnum.MONTH.getKey()),
                    "01FTBRZ5MR8V14HBZT72E4D9AY"); //random ULID
        }
        if(filter.containsKey(FilterExpensesParameterEnum.MONTH.getKey())
                && !filter.containsKey(FilterExpensesParameterEnum.YEAR.getKey())) {
            throw new MissingExpensesFilterException(
                    getMessageToException(FilterExpensesParameterEnum.YEAR.getKey()),
                    "01FTBS133FXBQJJA82Q1WJAX9C"); //random ULID
        }
        return filter.containsKey(FilterExpensesParameterEnum.YEAR.getKey())
                && filter.containsKey(FilterExpensesParameterEnum.MONTH.getKey());
    }

    private List<ExpensesDto> getAllExpensesFromMonthAndYear(MonthsEnum month, String year) {
        String from = month.getFirstDayForYear(year);
        String to = month.getLastDayForYear(year);

        return getAllExpensesBetweenDate(from, to);
    }

    private List<ExpensesDto> getAllExpensesBetweenDate(String fromDate, String toDate) {
        var user = userLogInfoService.getLoggedUserEntity();
        var dateSuffix = "T00:00:00.000000001";
        var fromInstantDate = LocalDateTime.parse(fromDate + dateSuffix);
        var toInstantDate = LocalDateTime.parse(toDate + dateSuffix);
        return expensesRepository.findAllByBetweenDate(user, fromInstantDate, toInstantDate)
                .stream()
                .map(expensesMapper::fromEntityToDtos)
                .collect(Collectors.toList());
    }
}
