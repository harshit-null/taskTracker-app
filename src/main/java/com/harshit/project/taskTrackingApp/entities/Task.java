package com.harshit.project.taskTrackingApp.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "status", nullable = false)
    private TaskStatus status;

    @Column(name = "priority", nullable = false)
    private TaskPriority priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_list_id")
    private TaskList taskList;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "updated", nullable = false)
    private LocalDateTime updated;

    @Column(name = "completed", nullable = true)
    private  LocalDateTime completed;

    @PrePersist
    public void prePersist() {
        this.created = LocalDateTime.now();
        this.updated = this.created;
    }

    @PreUpdate
    public void preUpdate() {
        this.updated = LocalDateTime.now();
    }

}