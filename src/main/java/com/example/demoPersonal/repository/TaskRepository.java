package com.example.demoPersonal.repository;

import com.example.demoPersonal.entity.Task;
import com.example.demoPersonal.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByUuid(UUID uuid);
    List<Task> findByDescription(String description);
    List<Task> findByStatus(Status status);
    List<Task> findByEmployeeIsNull();
}
