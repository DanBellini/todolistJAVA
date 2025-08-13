package com.todolist.api.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.todolist.api.dtos.UserDto;
import com.todolist.api.excepitions.user_error.UserConflictException;
import com.todolist.api.excepitions.user_error.UserNotFoundException;
import com.todolist.api.models.UserModel;
import com.todolist.api.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    //Metodo para verificar se ja possui usuário igual, e caso não possua, criar um novo
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

    // Método para buscar o usuário por nome
    public UserModel findByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException("Your username or password is wrong"));
    }

    // Método para verificar a senha do login
    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}