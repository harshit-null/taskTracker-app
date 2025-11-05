package com.harshit.project.taskTrackingApp.repository;

import com.harshit.project.taskTrackingApp.entities.Task;
import com.harshit.project.taskTrackingApp.entities.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskListRepository extends JpaRepository<TaskList, UUID> {
}
