package com.todolist.api.exceptions.subtask_error;

public class SubtaskNotFoundException extends RuntimeException{
    
    public SubtaskNotFoundException(String message){
        super(message);
    }
}
