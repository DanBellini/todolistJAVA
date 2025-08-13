package com.todolist.api.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.todolist.api.dtos.UserDto;
import com.todolist.api.excepitions.user_error.UserConflictException;
import com.todolist.api.models.UserModel;
import com.todolist.api.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserModel saveUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new UserConflictException("Username '" + userDto.getUsername() + "' already exists.");
        }

    UserModel user = UserModel.builder()
            .username(userDto.getUsername())
            .password(passwordEncoder.encode(userDto.getPassword()))
            .build();
        return userRepository.save(user);
    }
}
