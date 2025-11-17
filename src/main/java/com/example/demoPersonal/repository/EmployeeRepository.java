package com.example.demoPersonal.repository;


import com.example.demoPersonal.entity.Employee;
import com.example.demoPersonal.entity.enums.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);
    List<Employee> findByNameIgnoreCase(String name);
    List<Employee> findByPosition(Position position);
    boolean existsByEmail(String email);
}
