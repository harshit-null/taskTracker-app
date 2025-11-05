package com.harshit.project.taskTrackingApp.controllers;

import com.harshit.project.taskTrackingApp.dto.UserRequestDto;
import com.harshit.project.taskTrackingApp.entities.User;
import com.harshit.project.taskTrackingApp.repository.UserRepository;
import com.harshit.project.taskTrackingApp.services.impl.UserServiceImpl;
import com.harshit.project.taskTrackingApp.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class PublicController {
    private final UserRepository userRepository;

    private final UserServiceImpl userServiceImpl;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    public PublicController(UserRepository userRepository, UserServiceImpl userServiceImpl, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.userServiceImpl = userServiceImpl;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        User savedUser = userServiceImpl.signUp(user);
        System.out.println(savedUser);
        return new ResponseEntity<>(savedUser , HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDto loginRequest) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(),loginRequest.getPassword()));
        UserDetails userDetails = userServiceImpl.loadUserByUsername(loginRequest.getUserName());
        String accessToken = jwtUtil.generateAccessToken(userDetails.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());

        return new ResponseEntity<>(Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        ), HttpStatus.OK);

    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid refresh token"));
        }

        String username = jwtUtil.extractUsername(refreshToken);
        String newAccessToken = jwtUtil.generateAccessToken(username);

        return new ResponseEntity<>(Map.of(
                "accessToken", newAccessToken,
                "refreshToken", refreshToken // keep same refresh token
        ), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser(@RequestBody  UserRequestDto deleteRequest){
        userServiceImpl.deleteUser(deleteRequest.getUserName(), deleteRequest.getPassword());
        return new ResponseEntity<>("User deleted", HttpStatus.OK);
    }

}
