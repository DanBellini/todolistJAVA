package com.todolist.api.exceptions.task_error;

public class TaskNotFoundException extends RuntimeException{
    
public TaskNotFoundException(String message){
    super(message);
}
}
