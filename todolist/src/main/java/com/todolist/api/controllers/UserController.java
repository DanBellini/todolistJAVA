package com.todolist.api.controllers;

import com.todolist.api.dtos.LoginDto;
import com.todolist.api.dtos.UserDto;
import com.todolist.api.excepitions.user_error.UserNotFoundException;
import com.todolist.api.models.UserModel;
import com.todolist.api.security.JwtUtil;
import com.todolist.api.services.UserService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserDto userDto) {
        UserModel user = userService.saveUser(userDto);

        //Cria e retorna uma mensagem de sucesso
        String succesMenssage = "User '" + user.getUsername() + "' registered succesfully.";
        return ResponseEntity.status(HttpStatus.CREATED).body(succesMenssage);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody LoginDto loginDto) {
        UserModel user = userService.findByUsername(loginDto.getUsername());

        //Verifica se a senha de login é compativel com a senha criptografada do registro
        if(userService.validatePassword(loginDto.getPassword(), user.getPassword())){
            //Cria e retorna um token em caso de compatibilidade
            String token = jwtUtil.generateToken(user.getUsername());
            return ResponseEntity.ok(token);
        } else {
            //Retorna uma mensagem de erro
            throw new UserNotFoundException("Your username or password is wrong");
        }
    }
}
