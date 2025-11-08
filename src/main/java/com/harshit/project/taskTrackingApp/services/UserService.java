package com.harshit.project.taskTrackingApp.services;

import com.harshit.project.taskTrackingApp.entities.User;

import java.util.UUID;

public interface UserService {
    User signUp(User user);

    User login(String userName, String password);

    void deleteUser(String userName, String password);

    UUID getUserIdByUsername(String username);

    String getUsernameById(UUID userId);
}






