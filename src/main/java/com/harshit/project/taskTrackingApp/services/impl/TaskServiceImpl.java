package com.harshit.project.taskTrackingApp.services.impl;

import com.harshit.project.taskTrackingApp.entities.Task;
import com.harshit.project.taskTrackingApp.entities.TaskList;
import com.harshit.project.taskTrackingApp.entities.TaskPriority;
import com.harshit.project.taskTrackingApp.entities.TaskStatus;
import com.harshit.project.taskTrackingApp.repository.TaskListRepository;
import com.harshit.project.taskTrackingApp.repository.TaskRepository;
import com.harshit.project.taskTrackingApp.services.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {


    private final TaskRepository taskRepository;

    private final TaskListRepository taskListRepository;

    public TaskServiceImpl(TaskRepository taskRepository, TaskListRepository taskListRepository) {
        this.taskRepository = taskRepository;
        this.taskListRepository = taskListRepository;
    }

    @Override
    public List<Task> listTask(UUID taskListId) {
        return taskRepository.findByTaskListId(taskListId);
    }

    @Override
    public Task createTask(UUID taskListId, Task task) {
        if(null!= task.getId()){
            throw new IllegalArgumentException("task already has an id");
        }
        if(null == task.getTitle() || task.getTitle().isBlank()){
            throw new IllegalArgumentException("task must have a title");
        }
        TaskPriority taskPriority = Optional.ofNullable(task.getPriority()).orElse(TaskPriority.MEDIUM);
        TaskStatus taskStatus = TaskStatus.OPEN;

        TaskList taskList = taskListRepository.findById(taskListId).orElseThrow(() -> new IllegalArgumentException("Invalid Task List Id"));

        Task taskToSave = new Task(null,task.getTitle(), task.getDescription(), task.getDueDate(),taskStatus,taskPriority,taskList, LocalDateTime.now(),LocalDateTime.now(), null);

        return taskRepository.save(taskToSave);
    }

    @Override
    public Optional<Task> getTask(UUID taskListId, UUID taskId) {
         return taskRepository.findByTaskListIdAndId(taskListId , taskId);
    }

    @Override
    public Task updateTask(UUID taskListId, UUID taskId, Task task) {
        if(null == task.getStatus()){
            throw new IllegalArgumentException("Task must have a valid status");

        }
        Task existingTask = taskRepository.findByTaskListIdAndId(taskListId, taskId).orElseThrow(() -> new IllegalArgumentException("task not found"));

        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setDueDate(task.getDueDate());
        existingTask.setPriority(task.getPriority());
        existingTask.setStatus(task.getStatus());
        existingTask.setUpdated(LocalDateTime.now());

        return taskRepository.save(existingTask);
    }

    @Override
    @Transactional
    public void deleteTask(UUID taskListId, UUID id) {
        taskRepository.deleteByTaskListIdAndId(taskListId, id);
    }
}
