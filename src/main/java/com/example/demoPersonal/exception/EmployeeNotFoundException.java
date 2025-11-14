package com.example.demoPersonal.exception;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(Long id) {
        super("Error: Employee with ID: " + id + " not found.");
    }
}
