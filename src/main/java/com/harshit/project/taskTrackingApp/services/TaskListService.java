package com.harshit.project.taskTrackingApp.services;

import com.harshit.project.taskTrackingApp.entities.TaskList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskListService {
    List<TaskList> listTaskLists(UUID userId);
    TaskList createTaskList(TaskList taskList, UUID userId);
    Optional<TaskList> getTaskList(UUID id, UUID userId);
    TaskList updateTaskList(UUID id , TaskList taskList, UUID userId);
    void deleteTaskList(UUID id, UUID userId);
}
