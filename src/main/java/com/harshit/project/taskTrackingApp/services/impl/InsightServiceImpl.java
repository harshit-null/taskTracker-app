package com.harshit.project.taskTrackingApp.services.impl;

import com.harshit.project.taskTrackingApp.entities.TaskPriority;
import com.harshit.project.taskTrackingApp.entities.TaskStatus;
import com.harshit.project.taskTrackingApp.repository.TaskRepository;
import com.harshit.project.taskTrackingApp.services.InsightsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InsightServiceImpl implements InsightsService {

    private final TaskRepository taskRepository;

    @Override
    public Map<String, Object> generateWeeklyStats(UUID taskListId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekAgo = now.minusDays(7);

        int completed = taskRepository.findByTaskListIdAndStatusAndCompletedBetween(taskListId, TaskStatus.COMPLETED,weekAgo, now).size();

        int overdue = taskRepository.findByTaskListIdAndDueDateBeforeAndStatusNot(taskListId,now, TaskStatus.COMPLETED).size();

        // Pending tasks
        int pending = taskRepository
                .findByTaskListIdAndStatusIn(taskListId, List.of(TaskStatus.CLOSED, TaskStatus.OPEN))
                .size();

        Map<TaskPriority, Integer> priorityCount = new EnumMap<>(TaskPriority.class);
        for (TaskPriority priority : TaskPriority.values()) {
            int count = taskRepository.findByTaskListIdAndPriority(taskListId, priority).size();
            priorityCount.put(priority, count);
        }


        return Map.of( "completed", completed,
                "overdue", overdue,
                "pending",pending,
                "priorityBreakdown", priorityCount);
    }
}
