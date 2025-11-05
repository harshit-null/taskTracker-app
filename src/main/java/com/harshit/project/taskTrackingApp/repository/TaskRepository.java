package com.harshit.project.taskTrackingApp.repository;

import com.harshit.project.taskTrackingApp.entities.Task;
import com.harshit.project.taskTrackingApp.entities.TaskPriority;
import com.harshit.project.taskTrackingApp.entities.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByTaskListId(UUID taskListId);
    Optional<Task> findByTaskListIdAndId(UUID taskListId, UUID id);
    void deleteByTaskListIdAndId(UUID taskListId, UUID taskId);

      // find completed tasks in a specific period
    List<Task> findByTaskListIdAndStatusAndCompletedBetween(
            UUID taskListId,
            TaskStatus status,
            LocalDateTime start,
            LocalDateTime end
    );

    // find overdue tasks
    List<Task> findByTaskListIdAndDueDateBeforeAndStatusNot(
            UUID taskListId,
            LocalDateTime now,
            TaskStatus status
    );

    // find pending tasks
    List<Task> findByTaskListIdAndStatusIn(UUID taskListId, List<TaskStatus> statuses);

    // find by priority (for category breakdown)
    List<Task> findByTaskListIdAndPriority(UUID taskListId, TaskPriority priority);

}
