package com.harshit.project.taskTrackingApp.services;

import com.harshit.project.taskTrackingApp.entities.User;

public interface UserService {
    User signUp(User user);

    User login(String userName, String password);

    void deleteUser(String userName, String password);
}






