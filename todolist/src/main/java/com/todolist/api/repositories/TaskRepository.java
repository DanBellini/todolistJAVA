package com.todolist.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.todolist.api.models.TaskModel;
import com.todolist.api.models.UserModel;

@Repository
public interface TaskRepository extends JpaRepository<TaskModel, Long>{
    List<TaskModel> findByUser(UserModel user);
}
