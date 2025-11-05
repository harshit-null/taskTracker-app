package com.harshit.project.taskTrackingApp.dto;

public record ErrrorResponse(
        int status,
        String message,
        String details
) {
}
