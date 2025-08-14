package com.todolist.api.services.task;

import com.todolist.api.dtos.TaskDto;
import com.todolist.api.enums.PriorityEnum;
import com.todolist.api.enums.StatusEnum;
import com.todolist.api.models.TaskModel;
import com.todolist.api.models.UserModel;
import com.todolist.api.repositories.TaskRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskModel save(TaskDto taskDto, UserModel user) {
        // Cria a nova tarefa usando o construtor que aceita o DTO e o UserModel
        TaskModel newTask = new TaskModel(taskDto, user);
        
        // Salva a tarefa no banco de dados
        return taskRepository.save(newTask);
    }
    
    // Método para buscar todas as tarefas de um usuário específico
    public List<TaskModel> findAllByUser(UserModel user) {
        return taskRepository.findByUser(user);
    }
    
    // Novo método para buscar tarefas com filtros
    public List<TaskModel> findByUserWithFilters(
        UserModel user,
        StatusEnum status,
        PriorityEnum priority,
        LocalDate expirationDate
    ) {
        // Chama o método do repositório que lida com a lógica de filtros nulos
        return taskRepository.findByUserWithFilters(user, status, priority, expirationDate);
    }
}