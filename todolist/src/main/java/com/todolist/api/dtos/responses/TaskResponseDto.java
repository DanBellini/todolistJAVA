package com.todolist.api.dtos.responses;

import com.todolist.api.enums.PriorityEnum;
import com.todolist.api.enums.StatusEnum;
import com.todolist.api.models.TaskModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDto {
    private Long id;
    private String title;
    private String description;
    private LocalDate expirationDate;
    private StatusEnum status;
    private PriorityEnum priority;
    private UserResponseDto user;
    private List<SubtaskResponseDto> subtasks;

    // Construtor para converter de TaskModel para TaskResponseDto
    public TaskResponseDto(TaskModel task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.expirationDate = task.getExpirationDate();
        this.status = task.getStatus();
        this.priority = task.getPriority();
        this.user = new UserResponseDto(task.getUser());
        this.subtasks = task.getSubtasks().stream()
                .map(SubtaskResponseDto::new)
                .toList();
    }
}