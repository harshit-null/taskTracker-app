package com.harshit.project.taskTrackingApp.mappers;

import com.harshit.project.taskTrackingApp.dto.TaskDto;
import com.harshit.project.taskTrackingApp.entities.Task;


public interface TaskMapper {

    Task fromDto(TaskDto taskDto);

    TaskDto toDto(Task task);
}
