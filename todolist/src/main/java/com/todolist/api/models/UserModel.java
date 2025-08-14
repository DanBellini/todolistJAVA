package com.todolist.api.models;

import com.todolist.api.dtos.UserDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@Builder
public class UserModel {

    public UserModel(UserDto dto) {
        this.username = dto.getUsername();
        this.password = dto.getPassword();
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, length = 20, nullable = false)
    private String username;

    @Column(length = 150, nullable = false)
    private String password;
}
