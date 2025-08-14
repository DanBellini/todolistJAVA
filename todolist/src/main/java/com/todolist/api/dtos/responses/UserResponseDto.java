package com.todolist.api.dtos.responses;

import com.todolist.api.models.UserModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String username;

    // Construtor para converter de UserModel para UserResponseDto
    public UserResponseDto(UserModel user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }
}