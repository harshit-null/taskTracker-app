package com.harshit.project.taskTrackingApp.services;

import com.harshit.project.taskTrackingApp.entities.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskService {
    List<Task> listTask(UUID taskListId);
    Task createTask(UUID taskListId, Task task);
    Optional<Task> getTask(UUID taskListId, UUID taskId);
    Task updateTask(UUID taskListId, UUID taskId, Task task);
    void deleteTask(UUID taskListId, UUID id);
}
