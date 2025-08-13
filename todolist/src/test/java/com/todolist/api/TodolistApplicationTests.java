package com.todolist.api;

import com.todolist.api.dtos.UserDto;
import com.todolist.api.excepitions.user_error.UserConflictException;
import com.todolist.api.models.UserModel;
import com.todolist.api.repositories.UserRepository;
import com.todolist.api.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserDto userDto;
    private UserModel userModel;

    @BeforeEach
    void setUp() {
        userDto = new UserDto("testuser", "testpassword");
        userModel = new UserModel(1L, "testuser", "encodedpassword");
    }

    @Test
    void testSaveUserSuccess() {
        // Cenário: O usuário não existe no banco de dados.
        // O Mockito vai simular o comportamento do repositório.
        when(userRepository.existsByUsername(userDto.getUsername())).thenReturn(false);
        when(userRepository.save(any(UserModel.class))).thenReturn(userModel);

        // Ação: Chamar o método saveUser
        UserModel savedUser = userService.saveUser(userDto);

        // Verificação: Garantir que o usuário foi salvo com sucesso.
        assertNotNull(savedUser);
        assertEquals(userDto.getUsername(), savedUser.getUsername());
        verify(userRepository, times(1)).save(any(UserModel.class));
    }

    @Test
    void testSaveUserConflict() {
        // Cenário: O usuário já existe no banco de dados.
        when(userRepository.existsByUsername(userDto.getUsername())).thenReturn(true);

        // Ação: Chamar o método saveUser e verificar se a exceção é lançada.
        Exception exception = assertThrows(UserConflictException.class, () -> {
            userService.saveUser(userDto);
        });

        // Verificação: Garantir que a mensagem de erro está correta.
        String expectedMessage = "Username 'testuser' already exists.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        // Verificamos que o método save() nunca foi chamado.
        verify(userRepository, never()).save(any(UserModel.class));
    }
}