package com.todolist.api.dtos.responses;

import com.todolist.api.enums.PriorityEnum;
import com.todolist.api.enums.StatusEnum;
import com.todolist.api.models.SubtaskModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class SubtaskResponseDto {
    private Long id;
    private String title;
    private String description;
    private LocalDate expirationDate;
    private StatusEnum status;
    private PriorityEnum priority;

    public SubtaskResponseDto(SubtaskModel subtask) {
        this.id = subtask.getId();
        this.title = subtask.getTitle();
        this.description = subtask.getDescription();
        this.expirationDate = subtask.getExpirationDate();
        this.status = subtask.getStatus();
        this.priority = subtask.getPriority();
    }
}