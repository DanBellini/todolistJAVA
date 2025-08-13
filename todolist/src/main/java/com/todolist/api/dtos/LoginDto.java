package com.todolist.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginDto {
        
    @NotBlank (message = "Username cannot be blank")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @NotBlank (message = "Password cannot be blank")
    @Size(min = 3, max = 20, message = "Password must be between 3 and 20 characters")
    private String password;
    
}
