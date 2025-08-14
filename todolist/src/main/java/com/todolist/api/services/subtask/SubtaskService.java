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

    public SubtaskModel save(SubtaskDto subtaskDto, UserModel user, Long taskId) {
        // Encontra a TaskModel pelo ID. Se não encontrar, lança uma exceção.
        TaskModel task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + taskId));
        
        // Cria uma nova SubtaskModel com base no DTO, no usuário e na tarefa.
        SubtaskModel newSubtask = new SubtaskModel(subtaskDto, user, task);
        
        // Salva a nova subtask no banco de dados.
        return subtaskRepository.save(newSubtask);
    }
}