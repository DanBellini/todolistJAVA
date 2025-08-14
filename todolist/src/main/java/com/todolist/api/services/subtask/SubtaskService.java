package com.todolist.api.services.subtask;

import com.todolist.api.dtos.SubtaskDto;
import com.todolist.api.exceptions.task_error.TaskNotFoundException;
import com.todolist.api.models.SubtaskModel;
import com.todolist.api.models.TaskModel;
import com.todolist.api.models.UserModel;
import com.todolist.api.repositories.SubtaskRepository;
import com.todolist.api.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubtaskService {

    private final SubtaskRepository subtaskRepository;
    private final TaskRepository taskRepository;

    public SubtaskModel createSubtask(Long taskId, SubtaskDto subtaskDto, UserModel authenticatedUser) {
        // Encontra a tarefa pai, ou lança uma exceção se não for encontrada
        TaskModel task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));

        // Valida se o usuário autenticado é o dono da tarefa
        if (!task.getUser().getId().equals(authenticatedUser.getId())) {
            throw new IllegalArgumentException("User is not authorized to create subtasks for this task.");
        }

        // Cria a nova subtask
        SubtaskModel subtask = new SubtaskModel(subtaskDto, task);
        
        // Salva a subtask no banco de dados
        return subtaskRepository.save(subtask);
    }
}