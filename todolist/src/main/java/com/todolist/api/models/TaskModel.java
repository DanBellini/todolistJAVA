package com.todolist.api.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.todolist.api.dtos.TaskDto;
import com.todolist.api.enums.PriorityEnum;
import com.todolist.api.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
public class TaskModel {

    public TaskModel(TaskDto dto, UserModel user) {
        this.title = dto.getTitle();
        this.description = dto.getDescription();
        this.expirationDate = dto.getExpirationDate();
        this.user = user;

        // Lógica corrigida para lidar com null E string vazia
        if (dto.getStatus() == null || Objects.equals(dto.getStatus().name(), "")) {
            this.status = StatusEnum.NAO_REALIZADA;
        } else {
            this.status = dto.getStatus();
        }

        // Lógica corrigida para lidar com null E string vazia
        if (dto.getPriority() == null || Objects.equals(dto.getPriority().name(), "")) {
            this.priority = PriorityEnum.ALTA;
        } else {
            this.priority = dto.getPriority();
        }
    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate expirationDate;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Enumerated(EnumType.STRING)
    private PriorityEnum priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<SubtaskModel> subtasks = new ArrayList<>();
}