package com.kon.budget.service.integrations;

import com.kon.budget.enums.AuthenticationMessageEnum;
import com.kon.budget.exception.InvalidUsernameOrPasswordException;
import com.kon.budget.service.AuthenticationService;
import com.kon.budget.service.dtos.UserDetailsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthenticationServiceIntegrationTest extends IntegrationTestsData{

    @BeforeEach
    public void setup() {
        authenticationService = new AuthenticationService(userDetailsService,jwtService,authenticationManager);
    }

    @Test
    void shouldThrownAnInvalidUsernameOrPasswordExceptionWhenUsernameIsIncorrect() {
        initDatabaseWithUser();

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
        initDatabaseWithUser();

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
}
