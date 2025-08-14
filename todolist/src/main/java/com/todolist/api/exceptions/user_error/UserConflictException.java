package com.todolist.api.exceptions.user_error;

    public class UserConflictException extends RuntimeException{

        public UserConflictException(String message){
            super(message);
        }
}