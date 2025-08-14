package com.todolist.api.exceptions.user_error;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String message){
        super(message);
    }
    
}
