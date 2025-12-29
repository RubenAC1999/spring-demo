package com.example.demoPersonal.exception;

import java.util.UUID;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(UUID uuid) {
        super("Error: Employee with UUID: " + uuid + " not found.");
    }

    public EmployeeNotFoundException(String email) {
        super("Error: Employee with Email: " + email + " not found.");
    }
}
