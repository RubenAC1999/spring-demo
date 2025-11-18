package com.example.demoPersonal.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long id) {
        super("Error: Task with ID: " + id + " not found.");
    }
}
