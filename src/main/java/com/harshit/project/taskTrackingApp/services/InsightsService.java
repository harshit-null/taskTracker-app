package com.harshit.project.taskTrackingApp.services;

import java.util.Map;
import java.util.UUID;

public interface InsightsService {
    Map<String, Object>  generateWeeklyStats(UUID taskListId);

}
