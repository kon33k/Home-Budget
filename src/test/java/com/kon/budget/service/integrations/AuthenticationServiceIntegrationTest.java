package com.kon.budget.service.integrations;

import com.kon.budget.enums.AuthenticationMessageEnum;
import com.kon.budget.exception.InvalidUsernameOrPasswordException;
import com.kon.budget.service.AuthenticationService;
import com.kon.budget.service.JWTService;
import com.kon.budget.service.UserDetailsServiceImpl;
import com.kon.budget.service.dtos.UserDetailsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;

import javax.transaction.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class AuthenticationServiceIntegrationTest {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private AuthenticationService authenticationService;

    @BeforeEach
    public void setup() {
        authenticationService = new AuthenticationService(userDetailsService,jwtService,authenticationManager);
    }

    @Test
    void shouldThrownAnInvalidUsernameOrPasswordExceptionWhenUsernameIsIncorrect() {
        initUserDatabase();

        UserDetailsDto dto = new UserDetailsDto();
        dto.setUsername("invalidUsername");
        dto.setPassword("userPassword");

        //when
        var result = assertThrows(InvalidUsernameOrPasswordException.class,
                () -> authenticationService.createAuthenticationToken(dto));
        //then
        assertThat(result).isNotNull();
        assertThat(result.getMessage()).isEqualTo(AuthenticationMessageEnum.INVALID_USERNAME_OR_PASSWORD.getMessage());
    }

    @Test
    void shouldThrownAnInvalidUsernameOrPasswordExceptionWhenPasswordIsIncorrect() {
        initUserDatabase();

        UserDetailsDto dto = new UserDetailsDto();
        dto.setUsername("username");
        dto.setPassword("InvalidUserPassword");

        //when
        var result = assertThrows(InvalidUsernameOrPasswordException.class,
                () -> authenticationService.createAuthenticationToken(dto));
        //then
        assertThat(result).isNotNull();
        assertThat(result.getMessage()).isEqualTo(AuthenticationMessageEnum.INVALID_USERNAME_OR_PASSWORD.getMessage());
    }

    private void initUserDatabase() {
        UserDetailsDto dto = new UserDetailsDto();
        dto.setUsername("username");
        dto.setPassword("userPassword");
        userDetailsService.saveUser(dto);
    }
}
