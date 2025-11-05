package com.harshit.project.taskTrackingApp.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NonNull
    @Column(name = "user_name")
    private String userName;

    @Column(name = "email")
    private String email;

    @NonNull
    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<TaskList> taskLists = new ArrayList<>();

}