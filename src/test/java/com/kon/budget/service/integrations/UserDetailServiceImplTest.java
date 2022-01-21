package com.kon.budget.service.integrations;

import com.kon.budget.builder.AssetEntityBuilder;
import com.kon.budget.enums.AssetCategory;
import com.kon.budget.enums.AuthenticationMessageEnum;
import com.kon.budget.exception.UserAlreadyExistException;
import com.kon.budget.exception.UserNotFoundException;
import com.kon.budget.repository.entities.UserEntity;
import com.kon.budget.service.dtos.UserDetailsDto;
import org.junit.jupiter.api.Test;
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

public class UserDetailServiceImplTest extends IntegrationTestsData{

    @Test
    void shouldReturnUserWithUserNameAndPasswordFromDatabase() {
        ///given
        initDatabaseByMainUser();
        //when
        UserDetails result = userDetailsService.loadUserByUsername(MAIN_TEST_USERNAME);
        //then
        assertThat(result.getUsername()).isEqualTo(MAIN_TEST_USERNAME);
        assertThat(result.getPassword()).isEqualTo(MAIN_TEST_USER_PASSWORD);
    }

    @Test
    void shouldSaveUserInToDatabase() {
        //given
        UserDetailsDto dto = new UserDetailsDto();
        dto.setUsername(MAIN_TEST_USERNAME);
        dto.setPassword(MAIN_TEST_USER_PASSWORD);
        //when
        UUID userId = userDetailsService.saveUser(dto);
        //then
        assertThat(userId).isNotNull();
        Optional<UserEntity> byId = userRepository.findById(userId);
        UserEntity userEntity = byId.get(); // get bo by id zwraca optionala
        assertAll(
                () -> assertThat(userEntity.getUsername()).isEqualTo(MAIN_TEST_USERNAME),
                () -> assertThat(userEntity.getPassword()).contains(BCRYPT_PREFIX),
                () -> assertThat(userEntity.getPassword()).matches(Pattern.compile(BCRYPT_REGEX))
        );
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotFoundInDatabase() {
        //given
        initDatabaseByMainUser();
        //when
        UserNotFoundException result = assertThrows(UserNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("fakeUser"));
        //then
        assertThat(result.getMessage()).isEqualTo(AuthenticationMessageEnum.USER_NOT_FOUND.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyExistInDatabase() {
        //given
        initDatabaseByMainUser();
        UserDetailsDto dto = new UserDetailsDto();
        dto.setUsername(MAIN_TEST_USERNAME);
        dto.setPassword(MAIN_TEST_USER_PASSWORD);
        //when
        UserAlreadyExistException result = assertThrows(UserAlreadyExistException.class,
                () -> userDetailsService.saveUser(dto));
        //then
        assertThat(result.getMessage()).isEqualTo(AuthenticationMessageEnum.USER_ALREADY_EXIST.getMessage());
    }

    @Test
    void shouldRemoveUserWhichDoNotHaveAnyAssetsInDatabase() {
        //given
        initDatabaseByMainUser();

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
        initDatabaseByMainUser();
        var userEntity = userRepository.findByUsername(MAIN_TEST_USERNAME).get();
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
}
