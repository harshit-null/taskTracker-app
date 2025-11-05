package com.harshit.project.taskTrackingApp.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.action.internal.OrphanRemovalAction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "task_lists")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TaskList {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "taskList", cascade = {CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Task> tasks;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "updated", nullable = false)
    private LocalDateTime updated;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
