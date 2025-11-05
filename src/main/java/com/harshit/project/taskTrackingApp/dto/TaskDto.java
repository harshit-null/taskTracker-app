package com.harshit.project.taskTrackingApp.dto;

import com.harshit.project.taskTrackingApp.entities.TaskPriority;
import com.harshit.project.taskTrackingApp.entities.TaskStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskDto(
        UUID id,
        String title,
        String description,
        LocalDateTime dueDate,
        TaskPriority priority,
        TaskStatus status){
}


