package com.example.demoPersonal.exception;

public class EmployeeExistsException extends RuntimeException {
    public EmployeeExistsException(Long id) {
        super("Error: Employee with ID: " + id + " already exists.");
    }

    public EmployeeExistsException(String email) {
        super("Error: Employee with email: " + email + " already exists.");
    }
}
