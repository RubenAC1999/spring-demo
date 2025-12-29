package com.example.demoPersonal.exception;

import java.util.UUID;

public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException(UUID uuid) {
        super("Error: Project with UUID: " + uuid + " not found.");
    }
}
