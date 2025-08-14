package com.todolist.api.controllers;

import com.todolist.api.dtos.SubtaskDto;
import com.todolist.api.dtos.responses.SubtaskResponseDto;
import com.todolist.api.dtos.status.SubtaskStatusDto;
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

    @PostMapping("/{taskId}")
    public ResponseEntity<SubtaskResponseDto> createSubtask( // Altere o tipo de retorno aqui
            @PathVariable Long taskId,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody @Valid SubtaskDto subtaskDto) {

        String token = authorizationHeader.replace("Bearer ", "");

        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = jwtUtil.getUsernameFromToken(token);
        UserModel user = userService.findByUsername(username);

        SubtaskModel createdSubtask = subtaskService.createSubtask(taskId, subtaskDto, user);

        // Adicione esta linha para converter para o DTO de resposta
        SubtaskResponseDto responseDto = new SubtaskResponseDto(createdSubtask);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto); // Retorne o DTO
    }

    @PutMapping("/{subtaskId}/status")
    public ResponseEntity<SubtaskResponseDto> updateSubtaskStatus(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long subtaskId,
            @RequestBody @Valid SubtaskStatusDto subtaskStatusDto) {

        String token = authorizationHeader.replace("Bearer ", "");

        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        SubtaskModel updatedSubtask = subtaskService.updateStatus(subtaskId, subtaskStatusDto.getStatus());
        SubtaskResponseDto responseDto = new SubtaskResponseDto(updatedSubtask);

        return ResponseEntity.ok(responseDto);
    }
}