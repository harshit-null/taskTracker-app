package com.harshit.project.taskTrackingApp.controllers;

import com.harshit.project.taskTrackingApp.ai.InsightChatService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai/insights")
public class AiInsightsController {

    private final InsightChatService insightChatService;

    public AiInsightsController(InsightChatService insightChatService) {
        this.insightChatService = insightChatService;
    }

    @PostMapping("/ask")
    public String askInsight(@RequestBody String question) {
        return insightChatService.ask(question);
    }
}
