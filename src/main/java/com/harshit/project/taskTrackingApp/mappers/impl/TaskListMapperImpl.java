package com.harshit.project.taskTrackingApp.mappers.impl;

import com.harshit.project.taskTrackingApp.dto.TaskDto;
import com.harshit.project.taskTrackingApp.dto.TaskListDto;
import com.harshit.project.taskTrackingApp.entities.Task;
import com.harshit.project.taskTrackingApp.entities.TaskList;
import com.harshit.project.taskTrackingApp.entities.TaskStatus;
import com.harshit.project.taskTrackingApp.mappers.TaskListMapper;
import com.harshit.project.taskTrackingApp.mappers.TaskMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TaskListMapperImpl implements TaskListMapper {

    private final TaskMapper taskMapper;

    public TaskListMapperImpl(TaskMapper taskMapper){
        this.taskMapper = taskMapper;
    }

    @Override
    public TaskList fromDto(TaskListDto dto) {
        if (dto == null) return null;

        List tasks = Optional.ofNullable(dto.tasks())
                .map(ts -> ts.stream().map(taskMapper::fromDto).toList())
                .orElse(null);

        TaskList taskList = new TaskList();
        taskList.setId(dto.id());
        taskList.setTitle(dto.title());
        taskList.setDescription(dto.description()); // match DTO
        taskList.setTasks(tasks);
        // created/updated can be set in service layer
        return taskList;
    }


    @Override
    public TaskListDto toDto(TaskList taskList) {
        return new TaskListDto(
                taskList.getId(),
                taskList.getTitle(),
                taskList.getDescription(),
                Optional.ofNullable(taskList.getTasks())
                        .map(List::size).orElse(0),
                calculateTaskListProgress(taskList.getTasks()),
                Optional.ofNullable(taskList.getTasks())
                        .map(tasks -> tasks.stream().map(taskMapper::toDto).toList()).orElse(null)

        );
    }

    private double calculateTaskListProgress(List<Task> tasks){
        if (null == tasks){
            return 0.0;
        }
        long closedTaskCount = tasks.stream().filter(task -> TaskStatus.CLOSED == task.getStatus()).count();

        return (double) closedTaskCount/ tasks.size();
    }
}
