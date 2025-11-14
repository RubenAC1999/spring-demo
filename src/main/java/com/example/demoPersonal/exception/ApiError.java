package com.example.demoPersonal.exception;

import java.time.LocalDateTime;

public class ApiError {
    private String message;
    private int status;
    private String path;
    private LocalDateTime timestamp;

    public ApiError(String message, int status, String path, LocalDateTime timestamp) {
        this.message = message;
        this.status = status;
        this.path = path;
        this.timestamp = timestamp;
    }
}
