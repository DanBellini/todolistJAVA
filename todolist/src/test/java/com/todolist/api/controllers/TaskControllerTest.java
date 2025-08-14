package com.todolist.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todolist.api.dtos.TaskDto;
import com.todolist.api.dtos.status.SubtaskStatusDto;
import com.todolist.api.dtos.status.TaskStatusDto;
import com.todolist.api.enums.PriorityEnum;
import com.todolist.api.enums.StatusEnum;
import com.todolist.api.exceptions.task_error.TaskNotFoundException;
import com.todolist.api.models.SubtaskModel;
import com.todolist.api.models.TaskModel;
import com.todolist.api.models.UserModel;
import com.todolist.api.security.JwtUtil;
import com.todolist.api.security.SecurityTestConfig;
import com.todolist.api.services.UserService;
import com.todolist.api.services.subtask.SubtaskService;
import com.todolist.api.services.task.TaskService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({TaskController.class, SubtaskController.class})
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
    
    @SuppressWarnings("removal")
    @MockBean
    private SubtaskService subtaskService;

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
        mockTask.setSubtasks(List.of()); 
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
    
    @Test
    void whenInvalidToken_thenGetAllTasksReturnsUnauthorized() throws Exception {
        // Mock data
        String mockToken = "Bearer invalid_token";

        // Mockito: Retorna 'false' para simular um token inválido
        when(jwtUtil.validateToken(anyString())).thenReturn(false);

        // Simulação da requisição GET
        mockMvc.perform(get("/tasks")
                        .header("Authorization", mockToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        // Verificação: Garante que a lógica de negócio não foi executada
        verify(jwtUtil, times(1)).validateToken("invalid_token");
        verify(taskService, never()).findAllByUser(any(UserModel.class));
    }
    
    @Test
    void whenValidToken_thenGetAllTasksSuccessfully() throws Exception {
        // Mock data
        String mockToken = "Bearer valid_token";
        String mockUsername = "daniel";

        // Configura um usuário e uma task
        UserModel mockUser = new UserModel(1L, mockUsername, "password");
        TaskModel taskWithSubtasks = new TaskModel();
        taskWithSubtasks.setId(1L);
        taskWithSubtasks.setTitle("Tarefa com Subtarefas");
        taskWithSubtasks.setDescription("Descrição da Tarefa");
        taskWithSubtasks.setExpirationDate(LocalDate.of(2025, 8, 15));
        taskWithSubtasks.setStatus(StatusEnum.NAO_REALIZADA);
        taskWithSubtasks.setPriority(PriorityEnum.ALTA);
        taskWithSubtasks.setUser(mockUser);

        // Cria uma subtask de exemplo
        SubtaskModel subtask = new SubtaskModel();
        subtask.setId(10L);
        subtask.setTitle("Subtarefa 1");
        subtask.setDescription("Descrição da Subtarefa 1");
        subtask.setExpirationDate(LocalDate.of(2025, 8, 14));
        subtask.setStatus(StatusEnum.CONCLUIDA);
        subtask.setPriority(PriorityEnum.BAIXA);
        subtask.setTask(taskWithSubtasks);

        // Associa a subtask à task
        taskWithSubtasks.setSubtasks(List.of(subtask));

        // Configura o mock para retornar a lista de tasks com subtasks usando o novo método de filtro
        when(jwtUtil.validateToken(anyString())).thenReturn(true);
        when(jwtUtil.getUsernameFromToken(anyString())).thenReturn(mockUsername);
        when(userService.findByUsername(mockUsername)).thenReturn(mockUser);
        
        // Mockamos o findByUserWithFilters para o caso de não haver filtros (parâmetros null)
        when(taskService.findByUserWithFilters(
                any(UserModel.class),
                isNull(),
                isNull(),
                isNull()
        )).thenReturn(List.of(taskWithSubtasks));

        // Simulação da requisição GET e verificação
        mockMvc.perform(get("/tasks")
                        .header("Authorization", mockToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Tarefa com Subtarefas"))
                .andExpect(jsonPath("$[0].subtasks[0].id").value(10L)) // Verifica a subtask
                .andExpect(jsonPath("$[0].subtasks[0].title").value("Subtarefa 1")); // Verifica o título da subtask

        // Verificação: Garante que os métodos de serviço foram chamados
        verify(jwtUtil, times(1)).validateToken("valid_token");
        verify(taskService, times(1)).findByUserWithFilters(
                any(UserModel.class),
                isNull(),
                isNull(),
                isNull()
        );
    }
    
    // --- Novos testes para a funcionalidade de atualização de status ---

     @Test
    @DisplayName("Deve retornar 400 Bad Request ao tentar completar uma tarefa com subtasks incompletas")
    void whenUpdateTaskStatusToCompletedAndHasIncompleteSubtasks_thenReturnsBadRequest() throws Exception {
        // Dados simulados
        String mockToken = "Bearer valid_token";
        String mockUsername = "daniel";
        TaskStatusDto taskStatusDto = new TaskStatusDto(StatusEnum.CONCLUIDA);
        
        // Mock do usuário para a validação
        UserModel mockUser = new UserModel(1L, mockUsername, "password");
        when(jwtUtil.validateToken(anyString())).thenReturn(true);
        when(jwtUtil.getUsernameFromToken(anyString())).thenReturn(mockUsername);
        when(userService.findByUsername(mockUsername)).thenReturn(mockUser);

        // Mocka o serviço para lançar a exceção da regra de negócio
        when(taskService.updateStatus(anyLong(), eq(StatusEnum.CONCLUIDA)))
            .thenThrow(new IllegalArgumentException("Cannot complete a task with incomplete subtasks."));

        // Simulação da requisição PUT e verificação do status
        mockMvc.perform(put("/tasks/{taskId}/status", 1L)
                        .header("Authorization", mockToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskStatusDto)))
                .andExpect(status().isBadRequest());

        // Verificação: O método do serviço foi chamado
        verify(taskService, times(1)).updateStatus(anyLong(), eq(StatusEnum.CONCLUIDA));
    }

    @Test
    @DisplayName("Deve retornar 200 OK ao completar uma tarefa com todas as subtasks completas")
    void whenUpdateTaskStatusToCompletedAndAllSubtasksAreCompleted_thenReturnsOk() throws Exception {
        // Dados simulados
        String mockToken = "Bearer valid_token";
        String mockUsername = "daniel";
        TaskStatusDto taskStatusDto = new TaskStatusDto(StatusEnum.CONCLUIDA);
        
        // Mock do usuário para a validação
        UserModel mockUser = new UserModel(1L, mockUsername, "password");
        when(jwtUtil.validateToken(anyString())).thenReturn(true);
        when(jwtUtil.getUsernameFromToken(anyString())).thenReturn(mockUsername);
        when(userService.findByUsername(mockUsername)).thenReturn(mockUser);
        
        // Mocka a TaskModel que seria retornada pelo serviço
        TaskModel mockTask = new TaskModel();
        mockTask.setId(1L);
        mockTask.setStatus(StatusEnum.CONCLUIDA);
        mockTask.setUser(mockUser);

        // Mocka o serviço para retornar a task atualizada
        when(taskService.updateStatus(anyLong(), eq(StatusEnum.CONCLUIDA))).thenReturn(mockTask);

        // Simulação da requisição PUT e verificação da resposta
        mockMvc.perform(put("/tasks/{taskId}/status", 1L)
                        .header("Authorization", mockToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskStatusDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("CONCLUIDA"));

        // Verificação: O método do serviço foi chamado
        verify(taskService, times(1)).updateStatus(anyLong(), eq(StatusEnum.CONCLUIDA));
    }

    @Test
    @DisplayName("Deve retornar 200 OK ao atualizar o status de uma subtask")
    void whenUpdateSubtaskStatus_thenReturnsOk() throws Exception {
        // Dados simulados
        String mockToken = "Bearer valid_token";
        String mockUsername = "daniel";
        SubtaskStatusDto subtaskStatusDto = new SubtaskStatusDto(StatusEnum.CONCLUIDA);
        
        // Mocks do JWT e User
        when(jwtUtil.validateToken(anyString())).thenReturn(true);
        when(jwtUtil.getUsernameFromToken(anyString())).thenReturn(mockUsername);
        when(userService.findByUsername(mockUsername)).thenReturn(new UserModel(1L, mockUsername, "password"));
        
        // Mocka a SubtaskModel que seria retornada pelo serviço
        SubtaskModel mockSubtask = new SubtaskModel();
        mockSubtask.setId(10L);
        mockSubtask.setStatus(StatusEnum.CONCLUIDA);

        // Mocka o serviço de subtask para retornar a subtask atualizada
        when(subtaskService.updateStatus(anyLong(), eq(StatusEnum.CONCLUIDA))).thenReturn(mockSubtask);

        // Simulação da requisição PUT e verificação da resposta
        mockMvc.perform(put("/subtasks/{subtaskId}/status", 10L)
                        .header("Authorization", mockToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subtaskStatusDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.status").value("CONCLUIDA"));

        // Verificação: O método do serviço de subtask foi chamado
        verify(subtaskService, times(1)).updateStatus(anyLong(), eq(StatusEnum.CONCLUIDA));
    }

    // --- NOVOS TESTES PARA A ROTA DE DELEÇÃO DE TASKS ---

    @Test
    @DisplayName("Deve retornar 204 No Content ao deletar uma tarefa existente")
    void whenDeleteTask_thenReturnsNoContent() throws Exception {
        Long taskId = 1L;

        // Mocka o serviço para não fazer nada quando o método 'delete' for chamado
        doNothing().when(taskService).delete(taskId);

        mockMvc.perform(delete("/tasks/{taskId}", taskId))
                .andExpect(status().isNoContent());

        // Verifica se o método 'delete' do serviço foi chamado exatamente uma vez com o ID correto
        verify(taskService, times(1)).delete(taskId);
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found ao tentar deletar uma tarefa inexistente")
    void whenDeleteNonExistentTask_thenReturnsNotFound() throws Exception {
        Long nonExistentTaskId = 999L;

        // Mocka o serviço para lançar uma exceção de TaskNotFoundException
        doThrow(new TaskNotFoundException("Task not found with ID: " + nonExistentTaskId))
                .when(taskService).delete(nonExistentTaskId);

        mockMvc.perform(delete("/tasks/{taskId}", nonExistentTaskId))
                .andExpect(status().isNotFound());

        // Verifica se o método 'delete' do serviço foi chamado
        verify(taskService, times(1)).delete(nonExistentTaskId);
    }
}