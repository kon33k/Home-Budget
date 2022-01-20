package com.kon.budget.service.integrations;

import com.kon.budget.builder.AssetEntityBuilder;
import com.kon.budget.enums.AssetCategory;
import com.kon.budget.enums.AuthenticationMessageEnum;
import com.kon.budget.exception.UserAlreadyExistException;
import com.kon.budget.exception.UserNotFoundException;
import com.kon.budget.repository.AssetsRepository;
import com.kon.budget.repository.UserRepository;
import com.kon.budget.repository.entities.UserEntity;
import com.kon.budget.service.UserDetailsServiceImpl;
import com.kon.budget.service.dtos.UserDetailsDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@WithMockUser(username = "userName", password = "userPassword")
public class UserDetailServiceImplTest {

    public static final String USER_NAME = "userName";
    public static final String USER_PASSWORD = "userPassword";
    public static final String BCRYPT_PREFIX = "$2a$10$";
    public static final String BCRYPT_REGEX =  "^[$]2[abxy]?[$](?:0[4-9]|[12][0-9]|3[01])[$][./0-9a-zA-Z]{53}$";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AssetsRepository assetsRepository;

    @Test
    void shouldReturnUserWithUserNameAndPasswordFromDatabase() {
        ///given
        initDatabaseByUser();
        //when
        UserDetails result = userDetailsService.loadUserByUsername(USER_NAME);
        //then
        assertThat(result.getUsername()).isEqualTo(USER_NAME);
        assertThat(result.getPassword()).isEqualTo(USER_PASSWORD);
    }

    @Test
    void shouldSaveUserInToDatabase() {
        //given
        UserDetailsDto dto = new UserDetailsDto();
        dto.setUsername(USER_NAME);
        dto.setPassword(USER_PASSWORD);
        //when
        UUID userId = userDetailsService.saveUser(dto);
        //then
        assertThat(userId).isNotNull();
        Optional<UserEntity> byId = userRepository.findById(userId);
        UserEntity userEntity = byId.get(); // get bo by id zwraca optionala
        assertAll(
                () -> assertThat(userEntity.getUsername()).isEqualTo(USER_NAME),
                () -> assertThat(userEntity.getPassword()).contains(BCRYPT_PREFIX),
                () -> assertThat(userEntity.getPassword()).matches(Pattern.compile(BCRYPT_REGEX))
        );
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotFoundInDatabase() {
        //given
        initDatabaseByUser();
        //when
        UserNotFoundException result = assertThrows(UserNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("fakeUser"));
        //then
        assertThat(result.getMessage()).isEqualTo(AuthenticationMessageEnum.USER_NOT_FOUND.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyExistInDatabase() {
        //given
        initDatabaseByUser();
        UserDetailsDto dto = new UserDetailsDto();
        dto.setUsername(USER_NAME);
        dto.setPassword(USER_PASSWORD);
        //when
        UserAlreadyExistException result = assertThrows(UserAlreadyExistException.class,
                () -> userDetailsService.saveUser(dto));
        //then
        assertThat(result.getMessage()).isEqualTo(AuthenticationMessageEnum.USER_ALREADY_EXIST.getMessage());
    }

    @Test
    void shouldRemoveUserWhichDoNotHaveAnyAssetsInDatabase() {
        //given
        initDatabaseByUser();

        var userInDatabase = userRepository.findAll();
        assertThat(userInDatabase).hasSize(1);
        //when
        userDetailsService.deleteUser();
        //then
        var userInDatabaseAfterDelete = userRepository.findAll();
        assertThat(userInDatabaseAfterDelete).hasSize(0);

    }

    @Test
    void shouldRemoveUserWitchHaveOneAssetInDatabase() {
        //given
        initDatabaseByUser();
        var userEntity = userRepository.findByUsername(USER_NAME).get();
        initDatabaseWithUserAssets(userEntity);

        var userInDatabase = userRepository.findAll();
        assertThat(userInDatabase).hasSize(1);
        var assetsInDatabase = assetsRepository.findAll();
        assertThat(assetsInDatabase).hasSize(1);
        assertThat(assetsInDatabase.get(0).getUser()).isEqualTo(userEntity);
        //when
        userDetailsService.deleteUser();
        //then
        var userInDatabaseAfterDelete = userRepository.findAll();
        assertThat(userInDatabaseAfterDelete).hasSize(0);
        var assetsInDatabaseAfterDelete = assetsRepository.findAll();
        assertThat(assetsInDatabaseAfterDelete).hasSize(0);
    }

    private void initDatabaseWithUserAssets(UserEntity userEntity) {
        var assetsEntity = new AssetEntityBuilder()
                .withIncomeDate(Instant.now())
                .withUser(userEntity)
                .withAmount(BigDecimal.ONE)
                .withCategory(AssetCategory.OTHER)
                .build();

        assetsRepository.save(assetsEntity);
    }

    private void initDatabaseByUser() {
        UserEntity entity = new UserEntity();
        entity.setUsername(USER_NAME);
        entity.setPassword(USER_PASSWORD);
        userRepository.save(entity);
    }
}
