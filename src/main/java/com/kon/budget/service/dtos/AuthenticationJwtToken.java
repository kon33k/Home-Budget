package com.kon.budget.service.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthenticationJwtToken {

    private final String jwtToken;
}
