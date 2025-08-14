package com.todolist.api.controllers;

import com.todolist.api.dtos.TaskDto;
import com.todolist.api.dtos.responses.TaskResponseDto;
import com.todolist.api.models.TaskModel;
import com.todolist.api.models.UserModel;
import com.todolist.api.security.JwtUtil;
import com.todolist.api.services.UserService;
import com.todolist.api.services.task.TaskService;

import jakarta.validation.Valid;

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
    public ResponseEntity<List<TaskResponseDto>> getAllTasks(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = jwtUtil.getUsernameFromToken(token);
        UserModel user = userService.findByUsername(username);

        List<TaskModel> tasks = taskService.findAllByUser(user);
        
        List<TaskResponseDto> taskResponseDtos = tasks.stream()
                .map(TaskResponseDto::new)
                .toList();

        return ResponseEntity.ok(taskResponseDtos);
    }
}