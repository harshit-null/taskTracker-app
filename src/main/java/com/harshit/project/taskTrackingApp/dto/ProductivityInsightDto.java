package com.harshit.project.taskTrackingApp.dto;

import java.util.Map;

public record ProductivityInsightDto(String username,
        int completed,
        int overdue,
        int pending,
        Map<String, Integer> categories,
        String insights) { }
