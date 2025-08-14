package com.todolist.api.services.task;

import com.todolist.api.dtos.TaskDto;
import com.todolist.api.enums.PriorityEnum;
import com.todolist.api.enums.StatusEnum;
import com.todolist.api.exceptions.task_error.TaskNotFoundException;
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
    // Em com.todolist.api.services.task.TaskService

// ... (métodos save, findAllByUser, findByUserWithFilters, etc.)

    public TaskModel updateStatus(Long taskId, StatusEnum newStatus) {
        TaskModel task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + taskId));
        
        // Regra de Negócio: a task só pode ser concluída se não tiver subtasks
        // ou se todas as subtasks estiverem concluídas.
        if (newStatus == StatusEnum.CONCLUIDA && !task.getSubtasks().isEmpty()) {
            boolean hasIncompleteSubtasks = task.getSubtasks().stream()
                .anyMatch(subtask -> subtask.getStatus() != StatusEnum.CONCLUIDA);
            
            if (hasIncompleteSubtasks) {
                throw new IllegalArgumentException("Cannot complete a task with incomplete subtasks.");
            }
        }
        
        // Se a task for concluída, também conclui todas as suas subtasks
        if (newStatus == StatusEnum.CONCLUIDA) {
            task.getSubtasks().forEach(subtask -> subtask.setStatus(StatusEnum.CONCLUIDA));
        }
        
        task.setStatus(newStatus);

        return taskRepository.save(task);
    }
}