package com.harshit.project.taskTrackingApp.config;

import com.harshit.project.taskTrackingApp.ai.InsightTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    private final InsightTools insightTool;

    public AiConfig(InsightTools insightTool) {
        this.insightTool = insightTool;
    }


    @Bean
    public ChatClient chatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel).build();
    }
}
