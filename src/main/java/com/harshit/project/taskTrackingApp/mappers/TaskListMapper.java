package com.harshit.project.taskTrackingApp.mappers;

import com.harshit.project.taskTrackingApp.dto.TaskListDto;
import com.harshit.project.taskTrackingApp.entities.TaskList;

public interface TaskListMapper {
    TaskList fromDto(TaskListDto taskListDto);

    TaskListDto toDto(TaskList taskList);
}
