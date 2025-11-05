package com.harshit.project.taskTrackingApp.controllers;

import com.harshit.project.taskTrackingApp.dto.TaskListDto;
import com.harshit.project.taskTrackingApp.entities.TaskList;
import com.harshit.project.taskTrackingApp.mappers.TaskListMapper;
import com.harshit.project.taskTrackingApp.services.TaskListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "users/{user_id}/task-lists")
public class TaskListController {

    private final TaskListService taskListService;

    private final TaskListMapper taskListMapper;

    public TaskListController(TaskListService taskListService, TaskListMapper taskListMapper) {
        this.taskListService = taskListService;
        this.taskListMapper = taskListMapper;
    }


    @GetMapping
    public List<TaskListDto> listTaskList(@PathVariable("user_id")UUID userId){
        return taskListService.listTaskLists(userId)
                .stream().map(taskListMapper::toDto)
                .toList();

    }

    @PostMapping
    public TaskListDto createTaskList(@PathVariable("user_id")UUID userId,@RequestBody TaskListDto taskListDto){
        TaskList taskList = taskListService.createTaskList(taskListMapper.fromDto(taskListDto), userId );
        return taskListMapper.toDto(taskList);
    }

    @GetMapping(path = "/{task_list_id}")
    public ResponseEntity<TaskListDto> getTaskList(@PathVariable("user_id") UUID userId, @PathVariable("task_list_id") UUID id){
        return taskListService.getTaskList(id, userId).map(taskListMapper::toDto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(path = "/{task_list_id}")
    public TaskListDto updateTaskList(@PathVariable("user_id") UUID userId, @PathVariable("task_list_id") UUID taskListId, @RequestBody TaskListDto taskListDto){
        TaskList updatedTaskList = taskListService.updateTaskList(taskListId, taskListMapper.fromDto(taskListDto), userId );
        return taskListMapper.toDto(updatedTaskList);
    }

    @DeleteMapping(path = "/{task_list_id}")
    public ResponseEntity<?> deleteTaskList(@PathVariable("user_id") UUID userId, @PathVariable("task_list_id") UUID id){
        taskListService.deleteTaskList(id, userId);
        return new ResponseEntity<>("Task list deleted", HttpStatus.OK);

    }

}
