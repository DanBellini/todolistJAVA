package com.todolist.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.todolist.api.models.SubtaskModel;

public interface SubtaskRepository extends JpaRepository<SubtaskModel, Long>{
    
}
