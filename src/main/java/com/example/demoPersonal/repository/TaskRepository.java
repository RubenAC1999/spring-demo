package com.example.demoPersonal.repository;

import com.example.demoPersonal.entity.Task;
import com.example.demoPersonal.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByDescription(String description);
    List<Task> findByStatus(Status status);
    List<Task> findByEmployeeIsNull();
}
