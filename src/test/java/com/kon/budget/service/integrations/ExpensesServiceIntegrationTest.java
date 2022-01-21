package com.kon.budget.service.integrations;

import com.kon.budget.builder.ExpensesDtoBuilder;
import com.kon.budget.builder.ExpensesEntityBuilder;
import com.kon.budget.enums.ExpensesCategory;
import com.kon.budget.repository.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class ExpensesServiceIntegrationTest extends IntegrationTestsData{

    @Test
    void shouldSaveOneExpenseInDatabase() {
        //given
        initDatabaseWithUser();
        var dto = new ExpensesDtoBuilder()
                .withAmount(BigDecimal.ONE)
                .build();
        //when
        expensesService.setExpenses(dto);
        //then
        var entitiesInDatabase = expensesRepository.findAll();
        assertThat(entitiesInDatabase).hasSize(1);
        assertThat(entitiesInDatabase.get(0).getAmount()).isEqualTo(BigDecimal.ONE);
    }

    @Test
    void shouldDeleteExpensesFromDatabase() {
        //given
        var user =initDatabaseWithUser();
        var expensesId = initExpensesInDatabase(user);
        var dto = new ExpensesDtoBuilder()
                .withAmount(BigDecimal.ONE)
                .withId(expensesId)
                .build();

        var entitiesInDatabase = expensesRepository.findAll();
        assertThat(entitiesInDatabase).hasSize(1);
        //when
        expensesService.deleteExpenses(dto);
        //then
        var entitiesInDatabaseAfterDelete = expensesRepository.findAll();
        assertThat(entitiesInDatabaseAfterDelete).hasSize(0);
    }

    @Test
    void shouldUpdateExpenseInDatabase() {
        //given
        var user =initDatabaseWithUser();
        var expensesId = initExpensesInDatabase(user);
        var dto = new ExpensesDtoBuilder()
                .withAmount(BigDecimal.TEN)
                .withCategory(ExpensesCategory.FUN)
                .withId(expensesId)
                .build();

        var entityInDatabase = expensesRepository.findById(expensesId);
        var entity = entityInDatabase.get();
        assertThat(entity.getAmount()).isEqualTo(BigDecimal.ONE);
        assertThat(entity.getCategory()).isNull();
        //when
        expensesService.updateExpenses(dto);
        //then
        var entityInDatabaseAfterUpdate = expensesRepository.findById(expensesId);
        var entityAfterUpdate = entityInDatabase.get();
        assertThat(entityAfterUpdate.getAmount()).isEqualTo(BigDecimal.TEN);
        assertThat(entityAfterUpdate.getCategory()).isEqualTo(ExpensesCategory.FUN);
    }

    @Test
    void shouldReturnAllExpensesSavedInDatabase() {
        //given
        var user =initDatabaseWithUser();
        initExpensesInDatabase(user);
        initExpensesInDatabase(user);
        //when
        var result = expensesService.getAllExpenses();
        //then
        assertThat(result).hasSize(2);
    }
}
