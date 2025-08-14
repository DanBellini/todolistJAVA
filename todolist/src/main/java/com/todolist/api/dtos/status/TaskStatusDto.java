package com.todolist.api.dtos.status;

import com.todolist.api.enums.StatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatusDto {

    @NotNull(message = "Status cannot be null")
    private StatusEnum status;
}