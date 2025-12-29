package com.example.demoPersonal.dto.project;
import java.util.UUID;

public record ProjectResponseDTO (
    UUID uuid,
    String name
) {}
