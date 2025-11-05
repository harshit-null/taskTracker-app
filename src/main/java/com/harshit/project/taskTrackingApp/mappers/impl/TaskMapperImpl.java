package com.harshit.project.taskTrackingApp.mappers.impl;

import com.harshit.project.taskTrackingApp.dto.TaskDto;
import com.harshit.project.taskTrackingApp.entities.Task;
import com.harshit.project.taskTrackingApp.mappers.TaskMapper;
import org.springframework.stereotype.Component;

@Component
public class TaskMapperImpl implements TaskMapper {

    @Override
    public Task fromDto(TaskDto taskDto) {
        if (taskDto == null) {
            return null;
        }

        Task task = new Task();
        task.setId(taskDto.id());
        task.setTitle(taskDto.title());
        task.setDescription(taskDto.description());
        task.setDueDate(taskDto.dueDate());
        task.setStatus(taskDto.status());
        task.setPriority(taskDto.priority());
        // task.setTaskList(...); // optional, if your DTO ever includes it
        // task.setCreated(...);  // might be set automatically in service/db
        // task.setUpdated(...);

        return task;
    }

    @Override
    public TaskDto toDto(Task task) {
        if (task == null) {
            return null;
        }

        return new TaskDto(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getDueDate(),
                        task.getPriority(),
                        task.getStatus()
                );
    }
}
