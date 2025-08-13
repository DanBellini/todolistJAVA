package com.todolist.api.services.task;

import com.todolist.api.dtos.TaskDto;
import com.todolist.api.models.TaskModel;
import com.todolist.api.models.UserModel;
import com.todolist.api.repositories.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskModel save(TaskDto taskDto, UserModel user) {
        // Cria a nova tarefa usando o construtor que aceita o DTO e o UserModel
        TaskModel newTask = new TaskModel(taskDto, user);
        
        // Salva a tarefa no banco de dados
        return taskRepository.save(newTask);
    }
}