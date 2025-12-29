package com.example.demoPersonal.repository;

import com.example.demoPersonal.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByUuid(UUID uuid);
    List<Project> findByName(String name);
}
