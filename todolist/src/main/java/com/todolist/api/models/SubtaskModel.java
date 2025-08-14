package com.todolist.api.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.todolist.api.dtos.SubtaskDto;
import com.todolist.api.enums.PriorityEnum;
import com.todolist.api.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subtasks")
@Builder
public class SubtaskModel {
    
    public SubtaskModel(SubtaskDto dto, TaskModel task) {
        this.title = dto.getTitle();
        this.description = dto.getDescription();
        this.expirationDate = dto.getExpirationDate();
        this.status = dto.getStatus();
        this.priority = dto.getPriority();
        this.task = task;

        //Logica para Status
        if (dto.getStatus() == null || Objects.equals(dto.getStatus().name(), "")) {
            this.status = StatusEnum.NAO_REALIZADA;
        } else {
            this.status = dto.getStatus();
        }

        // Lógica para priority
        if (dto.getPriority() == null || Objects.equals(dto.getPriority().name(), "")) {
            this.priority = PriorityEnum.ALTA;
        } else {
            this.priority = dto.getPriority();
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    private LocalDate expirationDate;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Enumerated(EnumType.STRING)
    private PriorityEnum priority;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    @JsonBackReference
    private TaskModel task;
}