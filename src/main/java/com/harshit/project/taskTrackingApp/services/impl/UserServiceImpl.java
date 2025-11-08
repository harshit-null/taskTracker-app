package com.harshit.project.taskTrackingApp.services.impl;

import com.harshit.project.taskTrackingApp.entities.User;
import com.harshit.project.taskTrackingApp.repository.UserRepository;
import com.harshit.project.taskTrackingApp.services.UserService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User signUp(User user) {
        if (userRepository.findByUserName(user.getUserName()).isPresent()) {
            throw new RuntimeException("Username already exists please choose a different one");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User login(String userName, String password) {
        Optional<User> user = userRepository.findByUserName(userName);
        if (user.isPresent()) {
            User existingUser = user.get();
            if (passwordEncoder.matches(password, existingUser.getPassword())) {
                return existingUser;
            }
        }
        throw new RuntimeException("Invalid username or password");
    }

    @Override
    public void deleteUser(String userName, String password) {
        Optional<User> user = userRepository.findByUserName(userName);
        if ((user.isPresent())) {
            User existingUser = user.get();
            if (!passwordEncoder.matches(password, existingUser.getPassword())) {
                throw new BadCredentialsException("invalid username or password");
            }
            userRepository.delete(existingUser);
        }
    }

    @Override
    public UUID getUserIdByUsername(String username) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId(); //
    }

    @Override
    public String getUsernameById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getUserName();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Convert your User entity to Spring Security's UserDetails
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserName())
                .password(user.getPassword())
                .authorities("USER") // simple authority, you can enhance later
                .build();
    }
}
