package com.example.demoPersonal.exception;

import java.time.LocalDateTime;

public record ApiError (
        String message,
        int status,
        String path,
        LocalDateTime timestamp
) {}
