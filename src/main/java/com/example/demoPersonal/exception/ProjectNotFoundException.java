package com.example.demoPersonal.exception;

public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException(Long id) {
        super("Error: Project with ID: " + id + " not found.");
    }
}
