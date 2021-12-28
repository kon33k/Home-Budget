package com.kon.budget.controller;

import com.kon.budget.service.AuthenticationService;
import com.kon.budget.service.UserDetailsServiceImpl;
import com.kon.budget.service.dtos.AuthenticationJwtToken;
import com.kon.budget.service.dtos.UserDetailsDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @GetMapping
    public AuthenticationJwtToken getAuthenticationToken(@RequestBody UserDetailsDto userDetailsDto) {
        return authenticationService.createAuthenticationToken(userDetailsDto);
    }

    @PostMapping
    private UUID setUserDetails(@RequestBody UserDetailsDto userDetailsDto) {
        return userDetailsServiceImpl.saveUser(userDetailsDto);
    }
}
