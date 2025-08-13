package com.todolist.api.controllers;

import com.todolist.api.dtos.TaskDto;
import com.todolist.api.models.TaskModel;
import com.todolist.api.models.UserModel;
import com.todolist.api.security.JwtUtil;
import com.todolist.api.services.task.TaskService;
import com.todolist.api.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks") // A rota base para este controller deve ser "/tasks"
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public TaskController(TaskService taskService, UserService userService, JwtUtil jwtUtil) {
        this.taskService = taskService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<TaskModel> createTask(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody @Valid TaskDto taskDto) {

        // 1. Extrair o token do cabeçalho "Bearer "
        String token = authorizationHeader.replace("Bearer ", "");

        // 2. **Middleware de Validação**: Validar o token
        if (!jwtUtil.validateToken(token)) {
            // Se o token for inválido, retorna 401 Unauthorized imediatamente
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 3. **Lógica de Negócio**: Se o token for válido, prosseguir com a criação da tarefa
        
        // Pega o nome de usuário do token
        // Você vai precisar de um novo método no seu JwtUtil para extrair o username do token
        String username = jwtUtil.getUsernameFromToken(token);
        
        // Busca o UserModel pelo nome de usuário
        UserModel user = userService.findByUsername(username);
        
        // Chama o serviço para salvar a nova tarefa
        TaskModel createdTask = taskService.save(taskDto, user);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }
}