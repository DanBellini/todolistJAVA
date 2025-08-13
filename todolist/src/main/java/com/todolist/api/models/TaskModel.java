package com.todolist.api.models;

import com.todolist.api.dtos.TaskDto;
import com.todolist.api.enums.PriorityEnum;
import com.todolist.api.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
@Builder
public class TaskModel {

    public TaskModel(TaskDto dto, UserModel user) {
        this.title = dto.getTitle();
        this.description = dto.getDescription();
        this.expirationDate = dto.getExpirationDate();
        this.status = dto.getStatus();
        this.priority = dto.getPriority();
        this.user = user;
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

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubtaskModel> subtasks;
}