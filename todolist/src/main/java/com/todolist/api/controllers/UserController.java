package com.todolist.api.controllers;

import com.todolist.api.dtos.UserDto;
import com.todolist.api.models.UserModel;
import com.todolist.api.security.JwtUtil;
import com.todolist.api.services.UserService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserDto userDto) {
        UserModel user = userService.saveUser(userDto);

        // Gera token JWT para o username criado
        String token = jwtUtil.generateToken(user.getUsername());

        // Retorna token no corpo da resposta
        return ResponseEntity.ok(token);
    }
}
