package com.kon.budget.service;

import com.kon.budget.exception.UserAlreadyExistException;
import com.kon.budget.exception.UserNotFoundException;
import com.kon.budget.mapper.UserMapper;
import com.kon.budget.repository.UserRepository;
import com.kon.budget.service.dtos.UserDetailsDto;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    public static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class.getName());

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.info("Searching user = " + username);
        var entity = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        return new User(entity.getUsername(), entity.getPassword(), Collections.emptyList());
    }

    public UUID saveUser(UserDetailsDto userDetailsDto) {
        validateIfUserExist(userDetailsDto);
        var entity = userMapper.fromDtoToEntity(userDetailsDto);
        var savedEntity =  userRepository.save(entity);
        LOGGER.info("User saved = " + savedEntity);

        return savedEntity.getId();
    }

    private void validateIfUserExist(UserDetailsDto userDetailsDto) {
        var entity = userRepository.findByUsername(userDetailsDto.getUsername());

        if(entity.isPresent()) {
            throw new  UserAlreadyExistException();
        }
    }
}
