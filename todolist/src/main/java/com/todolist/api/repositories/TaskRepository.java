package com.todolist.api.repositories;

import com.todolist.api.enums.PriorityEnum;
import com.todolist.api.enums.StatusEnum;
import com.todolist.api.models.TaskModel;
import com.todolist.api.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskModel, Long> {

    // Método para buscar todas as tarefas de um usuário
    List<TaskModel> findByUser(UserModel user);

    // Novo método com @Query para buscar tarefas com filtros dinâmicos
        @Query("SELECT t FROM TaskModel t WHERE t.user = :user " +
        "AND (:status IS NULL OR t.status = :status) " +
        "AND (:priority IS NULL OR t.priority = :priority) " +
        "AND (CAST(:expirationDate AS date) IS NULL OR t.expirationDate = :expirationDate)")

        List<TaskModel> findByUserWithFilters(
        @Param("user") UserModel user,
        @Param("status") StatusEnum status,
        @Param("priority") PriorityEnum priority,
        @Param("expirationDate") LocalDate expirationDate
        );           

}