package com.kon.budget.service.integrations;

import com.kon.budget.enums.AssetCategory;
import com.kon.budget.repository.AssetsRepository;
import com.kon.budget.repository.ExpensesRepository;
import com.kon.budget.repository.UserRepository;
import com.kon.budget.repository.entities.AssetEntity;
import com.kon.budget.repository.entities.ExpensesEntity;
import com.kon.budget.repository.entities.UserEntity;
import com.kon.budget.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.Arrays.asList;

@SpringBootTest
@Transactional
@WithMockUser(username = "testUsernameMain", password = "testPasswordMain")
public abstract class IntegrationTestsData {

    @Autowired
    protected AssetsRepository assetsRepository;
    @Autowired
    protected AssetsService assetsService;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected UserDetailsServiceImpl userDetailsService;
    @Autowired
    protected JWTService jwtService;
    @Autowired
    protected AuthenticationManager authenticationManager;
    @Autowired
    protected AuthenticationService authenticationService;
    @Autowired
    protected ExpensesService expensesService;
    @Autowired
    protected ExpensesRepository expensesRepository;

    protected static final String MAIN_TEST_USERNAME = "testUsernameMain";
    protected static final String MAIN_TEST_USER_PASSWORD = "testPasswordMain";
    protected static final String BCRYPT_PREFIX = "$2a$10$";
    protected static final String BCRYPT_REGEX =  "^[$]2[abxy]?[$](?:0[4-9]|[12][0-9]|3[01])[$][./0-9a-zA-Z]{53}$";


    protected void initDatabaseWithUserAssets(UserEntity userEntity) {
        initDatabaseWithUserAssets(userEntity, LocalDateTime.now(), AssetCategory.RENT);
    }

    protected void initDatabaseWithUserAssets(UserEntity userEntity, String date) {
        String dateSuffix = "T00:00:00.000000001";

        initDatabaseWithUserAssets(userEntity, LocalDateTime.parse(date + dateSuffix), AssetCategory.RENT);
    }

    protected void initDatabaseWithUserAssets(UserEntity userEntity,
                                              String date,
                                              AssetCategory category) {
        String dateSuffix = "T00:00:00.000000001";

        initDatabaseWithUserAssets(userEntity, LocalDateTime.parse(date + dateSuffix), category);
    }

    private void initDatabaseWithUserAssets(UserEntity userEntity,
                                            LocalDateTime date,
                                            AssetCategory category) {
        var assetsEntity = AssetEntity.builder()
                .incomeDate(date)
                .user(userEntity)
                .amount(BigDecimal.ONE)
                .category(category)
                .build();

        assetsRepository.save(assetsEntity);
    }

    protected void initDataBaseByMainMockUserAndHisAssets() {
        UserEntity userEntity = initMainMockUserInToDatabase();
        AssetEntity entity1 = AssetEntity.builder()
                .amount(new BigDecimal(1))
                .incomeDate(LocalDateTime.now())
                .category(AssetCategory.OTHER)
                .user(userEntity)
                .build();
        AssetEntity entity2 = AssetEntity.builder()
                .amount(new BigDecimal(3))
                .incomeDate(LocalDateTime.now())
                .category(AssetCategory.SALARY)
                .user(userEntity)
                .build();
        AssetEntity entity3 = AssetEntity.builder()
                .amount(new BigDecimal(5))
                .incomeDate(LocalDateTime.now())
                .category(AssetCategory.RENT)
                .user(userEntity)
                .build();
        assetsRepository.saveAll(asList(entity1, entity2, entity3));
    }

    protected void initDataBaseBySecondMockUserAndHisAssets() {
        UserEntity userEntity = initSecondMockUserInToDatabase();
        AssetEntity entity1 = AssetEntity.builder()
                .amount(new BigDecimal(1))
                .incomeDate(LocalDateTime.now())
                .category(AssetCategory.OTHER)
                .user(userEntity)
                .build();
        AssetEntity entity2 = AssetEntity.builder()
                .amount(new BigDecimal(3))
                .incomeDate(LocalDateTime.now())
                .category(AssetCategory.SALARY)
                .user(userEntity)
                .build();
        AssetEntity entity3 = AssetEntity.builder()
                .amount(new BigDecimal(5))
                .incomeDate(LocalDateTime.now())
                .category(AssetCategory.RENT)
                .user(userEntity)
                .build();
        assetsRepository.saveAll(asList(entity1, entity2, entity3));
    }

    protected UserEntity initMainMockUserInToDatabase() {
        UserEntity user = new UserEntity();
        user.setUsername("testUsernameMain");
        user.setPassword("testPasswordMain");
        return userRepository.save(user);
    }



    protected UserEntity initSecondMockUserInToDatabase() {
        UserEntity user = new UserEntity();
        user.setUsername("testUsernameSecond");
        user.setPassword("testPasswordSecond");
        return userRepository.save(user);
    }


    protected void initDatabaseByMainUser() {
        UserEntity entity = new UserEntity();
        entity.setUsername(MAIN_TEST_USERNAME);
        entity.setPassword(MAIN_TEST_USER_PASSWORD);
        userRepository.save(entity);
    }

    protected UserEntity initDatabaseWithUser() {
        var user = new UserEntity();
        user.setUsername(MAIN_TEST_USERNAME);
        user.setPassword(MAIN_TEST_USER_PASSWORD);
        return userRepository.save(user);
    }

    protected UUID initExpensesInDatabase(UserEntity user) {
        var expenses = ExpensesEntity.builder()
                .user(user)
                .amount(BigDecimal.ONE)
                .build();

        var entity = expensesRepository.save(expenses);
        return entity.getId();
    }

    protected UUID initExpensesInDatabase(UserEntity user, String date) {
        var dateSuffix = "T00:00:00.000000001";

        var expenses = ExpensesEntity.builder()
                .user(user)
                .amount(BigDecimal.ONE)
                .purchaseDate(LocalDateTime.parse(date + dateSuffix))
                .build();

        var entity = expensesRepository.save(expenses);
        return entity.getId();
    }
}
