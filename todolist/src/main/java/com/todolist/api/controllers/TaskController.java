package com.todolist.api.controllers;

import com.todolist.api.dtos.TaskDto;
import com.todolist.api.dtos.responses.TaskResponseDto;
import com.todolist.api.dtos.status.TaskStatusDto;
import com.todolist.api.enums.PriorityEnum;
import com.todolist.api.enums.StatusEnum;
import com.todolist.api.models.TaskModel;
import com.todolist.api.models.UserModel;
import com.todolist.api.security.JwtUtil;
import com.todolist.api.services.UserService;
import com.todolist.api.services.task.TaskService;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
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
    public ResponseEntity<TaskResponseDto> createTask(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody @Valid TaskDto taskDto) {

        String token = authorizationHeader.replace("Bearer ", "");

        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = jwtUtil.getUsernameFromToken(token);
        UserModel user = userService.findByUsername(username);

        TaskModel createdTask = taskService.save(taskDto, user);

        TaskResponseDto responseDto = new TaskResponseDto(createdTask);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getAllTasks(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(required = false) StatusEnum status,
            @RequestParam(required = false) PriorityEnum priority,
            @RequestParam(required = false) LocalDate expirationDate) {

        String token = authorizationHeader.replace("Bearer ", "");

        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = jwtUtil.getUsernameFromToken(token);
        UserModel user = userService.findByUsername(username);

        // O TaskService agora lida com todos os casos, com ou sem filtros.
        List<TaskModel> tasks = taskService.findByUserWithFilters(user, status, priority, expirationDate);
        
        List<TaskResponseDto> taskResponseDtos = tasks.stream()
                .map(TaskResponseDto::new)
                .toList();

        return ResponseEntity.ok(taskResponseDtos);
    }

    @PutMapping("/{taskId}/status")
    public ResponseEntity<TaskResponseDto> updateTaskStatus(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long taskId,
            @RequestBody @Valid TaskStatusDto taskStatusDto) {

        String token = authorizationHeader.replace("Bearer ", "");

        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        TaskModel updatedTask = taskService.updateStatus(taskId, taskStatusDto.getStatus());
        TaskResponseDto responseDto = new TaskResponseDto(updatedTask);
        
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.delete(taskId);
        return ResponseEntity.noContent().build();
    }
}