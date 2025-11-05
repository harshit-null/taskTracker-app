package com.harshit.project.taskTrackingApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
        org.springframework.ai.autoconfigure.openai.OpenAiAutoConfiguration.class
})
public class TaskTrackingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskTrackingAppApplication.class, args);

	}

}