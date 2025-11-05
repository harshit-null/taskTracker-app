package com.harshit.project.taskTrackingApp.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class InsightChatService {

    private final ChatClient chatClient;

    public InsightChatService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String ask(String question) {
        return chatClient
                .prompt(question)
                //.user(question)
                .call()
                .content();
    }
}
