package com.todolist.api.excepitions.user_error;

    public class UserConflictException extends RuntimeException{

        public UserConflictException(String message){
            super(message);
        }
}