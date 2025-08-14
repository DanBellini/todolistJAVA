package com.todolist.api.models;

import com.todolist.api.dtos.SubtaskDto;
import com.todolist.api.enums.PriorityEnum;
import com.todolist.api.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subtasks")
@Builder
public class SubtaskModel {
    
    public SubtaskModel(SubtaskDto dto, UserModel user, TaskModel task) {
        this.title = dto.getTitle();
        this.description = dto.getDescription();
        this.expirationDate = dto.getExpirationDate();
        this.status = dto.getStatus();
        this.priority = dto.getPriority();
        this.user = user;
        this.task = task;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEnum status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PriorityEnum priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private TaskModel task;
}