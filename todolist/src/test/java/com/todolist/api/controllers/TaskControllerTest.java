package com.todolist.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todolist.api.dtos.TaskDto;
import com.todolist.api.enums.PriorityEnum;
import com.todolist.api.enums.StatusEnum;
import com.todolist.api.models.TaskModel;
import com.todolist.api.models.UserModel;
import com.todolist.api.security.JwtUtil;
import com.todolist.api.security.SecurityTestConfig;
import com.todolist.api.services.UserService;
import com.todolist.api.services.task.TaskService;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@Import(SecurityTestConfig.class) 
@RequiredArgsConstructor
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("removal")
    @MockBean
    private JwtUtil jwtUtil;

    @SuppressWarnings("removal")
    @MockBean
    private UserService userService;

    @SuppressWarnings("removal")
    @MockBean
    private TaskService taskService;

@Test
void whenValidToken_thenCreateTaskSuccessfully() throws Exception {
    // Mock data
    TaskDto taskDto = new TaskDto("Teste", "Descrição", LocalDate.now(), StatusEnum.NAO_REALIZADA, PriorityEnum.ALTA);
    String mockToken = "Bearer valid_token";
    String mockUsername = "daniel";
    
    // Construtor para o UserModel
    UserModel mockUser = new UserModel(1L, mockUsername, "password");
    
    // Crie o objeto TaskModel usando um construtor padrão
    TaskModel mockTask = new TaskModel();
    mockTask.setId(1L);
    mockTask.setTitle("Teste");
    mockTask.setDescription("Descrição");
    mockTask.setExpirationDate(LocalDate.now());
    mockTask.setStatus(StatusEnum.NAO_REALIZADA);
    mockTask.setPriority(PriorityEnum.ALTA);
    mockTask.setUser(mockUser);
    // ------------------------------------------

    // Mockito: Define o comportamento dos mocks
    when(jwtUtil.validateToken(anyString())).thenReturn(true);
    when(jwtUtil.getUsernameFromToken(anyString())).thenReturn(mockUsername);
    when(userService.findByUsername(mockUsername)).thenReturn(mockUser);
    when(taskService.save(any(TaskDto.class), any(UserModel.class))).thenReturn(mockTask);

    // Simulação da requisição POST
    mockMvc.perform(post("/tasks")
                    .header("Authorization", mockToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(taskDto)))
            .andExpect(status().isCreated());

    // Verificação: Garante que os métodos de serviço foram chamados
    verify(jwtUtil, times(1)).validateToken("valid_token");
    verify(userService, times(1)).findByUsername(mockUsername);
    verify(taskService, times(1)).save(any(TaskDto.class), any(UserModel.class));
}

    @Test
    void whenInvalidToken_thenReturnsUnauthorized() throws Exception {
        // Mock data
        TaskDto taskDto = new TaskDto("Teste", "Descrição", LocalDate.now(), StatusEnum.NAO_REALIZADA, PriorityEnum.ALTA);
        String mockToken = "Bearer invalid_token";
        
        // Mockito: Retorna 'false' para simular um token inválido
        when(jwtUtil.validateToken(anyString())).thenReturn(false);

        // Simulação da requisição POST
        mockMvc.perform(post("/tasks")
                        .header("Authorization", mockToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isUnauthorized());

        // Verificação: Garante que a lógica de negócio não foi executada
        verify(jwtUtil, times(1)).validateToken("invalid_token");
        verify(userService, never()).findByUsername(anyString());
        verify(taskService, never()).save(any(TaskDto.class), any(UserModel.class));
    }
}