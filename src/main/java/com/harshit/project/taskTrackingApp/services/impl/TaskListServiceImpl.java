package com.harshit.project.taskTrackingApp.services.impl;

import com.harshit.project.taskTrackingApp.entities.TaskList;
import com.harshit.project.taskTrackingApp.entities.User;
import com.harshit.project.taskTrackingApp.repository.TaskListRepository;
import com.harshit.project.taskTrackingApp.repository.UserRepository;
import com.harshit.project.taskTrackingApp.services.TaskListService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskListServiceImpl implements TaskListService {

    private final TaskListRepository taskListRepository;

    private final UserRepository userRepository;

    public TaskListServiceImpl(TaskListRepository taskListRepository, UserRepository userRepository){
        this.taskListRepository= taskListRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<TaskList> listTaskLists(UUID userId) {
        return taskListRepository.findAll()
                .stream()
                .filter(taskList -> taskList.getUser().getId().equals(userId))
                .toList();
    }

    @Override
    public TaskList createTaskList(TaskList taskList, UUID userId) {

        if(taskList.getId()!=null){
            throw new IllegalArgumentException("task list already has a id");
        }

        if(null == taskList.getTitle()|| taskList.getTitle().isBlank()){
            throw new IllegalArgumentException("task list title must have a title");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("user not found"));

        LocalDateTime localDateTime = LocalDateTime.now();

        return taskListRepository.save(new TaskList(null,taskList.getTitle(),taskList.getDescription(),null,localDateTime,localDateTime, user));

    }

    @Override
    public Optional<TaskList> getTaskList(UUID id, UUID userId) {
        return taskListRepository.findById(userId)
                .filter(taskList -> taskList.getUser().getId().equals(userId));
    }

    @Override
    public TaskList updateTaskList(UUID id, TaskList taskList, UUID userId) {

        TaskList existingTaskList = taskListRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("task list not found"));

        if (!existingTaskList.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You do not own this task list");
        }

        existingTaskList.setDescription(taskList.getDescription());
        existingTaskList.setTitle(taskList.getTitle());
        existingTaskList.setUpdated(LocalDateTime.now());
         return taskListRepository.save(existingTaskList);
    }

    @Override
    public void deleteTaskList(UUID id, UUID userId) {

        TaskList taskList = taskListRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task list not found"));

        if (!taskList.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You do not own this task list");
        }

        taskListRepository.deleteById(id);
    }
}
