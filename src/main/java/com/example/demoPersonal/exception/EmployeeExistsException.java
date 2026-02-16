package com.example.demoPersonal.exception;

public class EmployeeExistsException extends RuntimeException {
    public EmployeeExistsException(String email) {
        super("Error: Employee with email: " + email + " already exists.");
    }
}
