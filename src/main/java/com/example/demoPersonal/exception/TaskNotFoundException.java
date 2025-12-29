package com.example.demoPersonal.exception;

import java.util.UUID;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(UUID uuid) {
        super("Error: Task with UUID: " + uuid + " not found.");
    }
}
