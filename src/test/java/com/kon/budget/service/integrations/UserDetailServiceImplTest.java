package com.kon.budget.service.integrations;

import com.kon.budget.enums.AuthenticationMessageEnum;
import com.kon.budget.exception.UserAlreadyExistException;
import com.kon.budget.exception.UserNotFoundException;
import com.kon.budget.repository.UserRepository;
import com.kon.budget.repository.entities.UserEntity;
import com.kon.budget.service.UserDetailsServiceImpl;
import com.kon.budget.service.dtos.UserDetailsDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import javax.transaction.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class UserDetailServiceImplTest {

    public static final String USER_NAME = "userName";
    public static final String USER_PASSWORD = "userPassword";
    public static final String BCRYPT_PREFIX = "$2a$10$";
    public static final String BCRYPT_REGEX =  "^[$]2[abxy]?[$](?:0[4-9]|[12][0-9]|3[01])[$][./0-9a-zA-Z]{53}$";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void shouldReturnUserWithUserNameAndPasswordFromDatabase() {
        ///given
        initDatabase();
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
        initDatabase();

        //when
        UserNotFoundException result = assertThrows(UserNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("fakeUser"));
        //then
        assertThat(result.getMessage()).isEqualTo(AuthenticationMessageEnum.USER_NOT_FOUND.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyExistInDatabase() {
        //given
        initDatabase();
        UserDetailsDto dto = new UserDetailsDto();
        dto.setUsername(USER_NAME);
        dto.setPassword(USER_PASSWORD);
        //when
        UserAlreadyExistException result = assertThrows(UserAlreadyExistException.class,
                () -> userDetailsService.saveUser(dto));
        //then
        assertThat(result.getMessage()).isEqualTo(AuthenticationMessageEnum.USER_ALREADY_EXIST.getMessage());
    }

    private void initDatabase() {
        UserEntity entity = new UserEntity();
        entity.setUsername(USER_NAME);
        entity.setPassword(USER_PASSWORD);
        userRepository.save(entity);
    }
}
