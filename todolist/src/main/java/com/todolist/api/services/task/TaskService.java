package com.todolist.api.services.task;

import com.todolist.api.dtos.TaskDto;
import com.todolist.api.models.TaskModel;
import com.todolist.api.models.UserModel;
import com.todolist.api.repositories.TaskRepository;

import lombok.RequiredArgsConstructor;

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
    
    // Método para buscar todas as tarefas
    public List<TaskModel> findAll() {
        return taskRepository.findAll();
    }
    
    //Método para buscar todas as tarefas de um usuário específico
    public List<TaskModel> findAllByUser(UserModel user) {
        return taskRepository.findByUser(user);
    }
}