package com.kon.budget.service;

import com.kon.budget.service.dtos.AuthenticationJwtToken;
import com.kon.budget.service.dtos.UserDetailsDto;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final UserDetailsService userDetailsService;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationJwtToken createAuthenticationToken(UserDetailsDto userDetailsDto) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userDetailsDto.getUsername(), userDetailsDto.getPassword()
        ));

        var userDetails = userDetailsService.loadUserByUsername(userDetailsDto.getUsername());
        var jwtToken = jwtService.generateJWTToken(userDetails);

        return new AuthenticationJwtToken(jwtToken);
    }
}
