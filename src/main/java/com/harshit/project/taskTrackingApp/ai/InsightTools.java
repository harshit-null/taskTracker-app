package com.harshit.project.taskTrackingApp.ai;

import com.harshit.project.taskTrackingApp.services.InsightsService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.UUID;

@Component
public class InsightTools {

    private final InsightsService insightsService;

    public InsightTools(InsightsService insightsService) {
        this.insightsService = insightsService;
    }


    @Tool(
        description = "Fetch weekly task statistics for a given task list ID"
    )
    public Map<String, Object> getWeeklyStats(@ToolParam String taskListId) {
        return insightsService.generateWeeklyStats(UUID.fromString(taskListId));
    }
}
