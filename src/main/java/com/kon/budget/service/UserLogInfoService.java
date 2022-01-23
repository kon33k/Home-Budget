package com.kon.budget.service;

import com.kon.budget.exception.UserNotFoundException;
import com.kon.budget.repository.UserRepository;
import com.kon.budget.repository.entities.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserLogInfoService {
    //pozdyskuje zalogowanego usera

    private final UserRepository userRepository;

    //pozdyskuje uzytwkoika z kontekstu spring security
    public UserEntity getLoggedUserEntity() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var username = ((User)authentication.getPrincipal()).getUsername();

        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }
}
