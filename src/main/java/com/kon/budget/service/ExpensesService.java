package com.kon.budget.service;

import com.kon.budget.mapper.ExpensesMapper;
import com.kon.budget.repository.ExpensesRepository;
import com.kon.budget.repository.entities.ExpensesEntity;
import com.kon.budget.repository.entities.UserEntity;
import com.kon.budget.service.dtos.ExpensesDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ExpensesService {

    private final ExpensesRepository expensesRepository;
    private final ExpensesMapper expensesMapper;
    private final UserLogInfoService userLogInfoService;

    public void setExpenses(ExpensesDto dto) {
        var user = userLogInfoService.getLoggedUserEntity();

        var entity = expensesMapper.fromDtoToEntity(dto, user);

        expensesRepository.save(entity);
    }

    public void deleteExpenses(ExpensesDto dto) {
        UserEntity user = userLogInfoService.getLoggedUserEntity();
        var entity = expensesMapper.fromDtoToEntity(dto, user);
        expensesRepository.delete(entity);
    }

    /*
    na podstaie id z dto wyciaga encje
    */

    @Transactional
    public void updateExpenses(ExpensesDto dto) {
        var entity = expensesRepository.findById(dto.getId());
        if (entity.isPresent()) {
            updateExpenses(entity.get(), dto);
        }
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

    public List<ExpensesDto> getAllExpenses() {
        var user = userLogInfoService.getLoggedUserEntity();
        var allExpenses = expensesRepository.findAllByUser(user);
        return expensesMapper.fromEntitiesToDtos(allExpenses);
    }
}
