package com.todolist.api.services;

import com.todolist.api.dtos.UserDto;
import com.todolist.api.excepitions.user_error.UserConflictException;
import com.todolist.api.excepitions.user_error.UserNotFoundException;
import com.todolist.api.models.UserModel;
import com.todolist.api.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserDto userDto;
    private UserModel userModel;
    private String rawPassword;
    private String encodedPassword;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder(); // Instancia a classe real
        
        // Crie um senha real e sua versão encodada
        rawPassword = "testpassword";
        encodedPassword = passwordEncoder.encode(rawPassword);

        userDto = new UserDto("testuser", rawPassword);
        userModel = new UserModel(1L, "testuser", encodedPassword);
    }

    //Este teste verifica se um novo usuário é criado e salvo corretamente quando ele não 
    //existe no banco de dados.
    @Test
    void testSaveUserSuccess() {
        when(userRepository.existsByUsername(userDto.getUsername())).thenReturn(false);
        when(userRepository.save(any(UserModel.class))).thenReturn(userModel);

        UserModel savedUser = userService.saveUser(userDto);

        assertNotNull(savedUser);
        assertEquals(userDto.getUsername(), savedUser.getUsername());
        verify(userRepository, times(1)).save(any(UserModel.class));
    }

    //Este teste verifica o cenário de erro de registro. Ele garante que, se o nome de usuário já
    //existir no banco de dados, o método saveUser lança a exceção UserConflictException e que o método 
    //save do repositório nunca é chamado.
    @Test
    void testSaveUserConflict() {
        when(userRepository.existsByUsername(userDto.getUsername())).thenReturn(true);

        assertThrows(UserConflictException.class, () -> {
            userService.saveUser(userDto);
        });

        verify(userRepository, never()).save(any(UserModel.class));
    }

    //Testa o novo método findByUsername. Ele verifica se a busca por um username existente retorna o UserModel correto,
    @Test
    void testFindUserByUsernameSuccess() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userModel));
        
        UserModel foundUser = userService.findByUsername("testuser");
        
        assertNotNull(foundUser);
        assertEquals(userModel.getUsername(), foundUser.getUsername());
    }
        
    //Testa o tratamento de erro na busca por usuário. Ele assegura que o método findByUsername lança a exceção 
    //UserNotFoundException quando o username não é encontrado no banco de dados.
    @Test
    void testFindUserByUsernameNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.findByUsername("nonexistentuser");
        });
    }

    //Testa a lógica de validação de senha. Ele verifica que o método validatePassword retorna true quando a senha 
    //fornecida pelo usuário corresponde à senha criptografada.
    @Test
    void testValidatePasswordCorrectly() {
        boolean passwordMatches = userService.validatePassword(rawPassword, encodedPassword);
        assertTrue(passwordMatches);
    }

    //esta o cenário de senha incorreta. Ele garante que o método validatePassword retorna false quando a senha
    //fornecida não corresponde à senha criptografada.
    @Test
    void testValidatePasswordIncorrectly() {
        boolean passwordMatches = userService.validatePassword("wrongpassword", encodedPassword);
        assertFalse(passwordMatches);
    }
}