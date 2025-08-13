package com.todolist.api.dtos;

import java.time.LocalDate;

import com.todolist.api.enums.PriorityEnum;
import com.todolist.api.enums.StatusEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    @NotBlank(message = "Title cannot be blank.")
    private String title;

    @NotBlank(message = "Description cannot be blank.")
    private String description;

    @NotNull(message = "ExpirationDate cannot be blank.")
    private LocalDate expirationDate;

    @NotNull(message = "Status cannot be blank.")
    private StatusEnum status;

    @NotNull(message = "Priority cannot be blank.")
    private PriorityEnum priority;
}