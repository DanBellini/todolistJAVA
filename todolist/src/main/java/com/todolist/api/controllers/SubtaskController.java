package com.todolist.api.controllers;

import com.todolist.api.dtos.SubtaskDto;
import com.todolist.api.models.SubtaskModel;
import com.todolist.api.models.UserModel;
import com.todolist.api.security.JwtUtil;
import com.todolist.api.services.UserService;
import com.todolist.api.services.subtask.SubtaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subtasks")
public class SubtaskController {

    
    private final SubtaskService subtaskService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public SubtaskController(SubtaskService subtaskService, UserService userService, JwtUtil jwtUtil) {
        this.subtaskService = subtaskService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/tasks/{taskId}") 
    public ResponseEntity<SubtaskModel> createSubtask(
            @PathVariable Long taskId,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody @Valid SubtaskDto subtaskDto) {

        // 1. Extrai e valida o token JWT
        String token = authorizationHeader.replace("Bearer ", "");
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 2. Extrai o usuário do token
        String username = jwtUtil.getUsernameFromToken(token);
        UserModel user = userService.findByUsername(username);

        // 3. Chama o serviço para salvar a subtask
        SubtaskModel createdSubtask = subtaskService.save(subtaskDto, user, taskId);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdSubtask);
    }
}