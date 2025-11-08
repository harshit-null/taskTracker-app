package com.harshit.project.taskTrackingApp.controllers;

import com.harshit.project.taskTrackingApp.dto.UserRequestDto;
import com.harshit.project.taskTrackingApp.entities.User;
import com.harshit.project.taskTrackingApp.repository.UserRepository;
import com.harshit.project.taskTrackingApp.services.RefreshTokenService;
import com.harshit.project.taskTrackingApp.services.impl.RefreshTokenImpl;
import com.harshit.project.taskTrackingApp.services.impl.UserServiceImpl;
import com.harshit.project.taskTrackingApp.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class PublicController {
    private final UserRepository userRepository;

    private final UserServiceImpl userServiceImpl;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final RefreshTokenService refreshTokenService;

    public PublicController(UserRepository userRepository, UserServiceImpl userServiceImpl, AuthenticationManager authenticationManager, JwtUtil jwtUtil, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.userServiceImpl = userServiceImpl;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        User savedUser = userServiceImpl.signUp(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDto loginRequest) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
        UserDetails userDetails = userServiceImpl.loadUserByUsername(loginRequest.getUserName());
        UUID userId = userServiceImpl.getUserIdByUsername(userDetails.getUsername());

        String accessToken = jwtUtil.generateAccessToken(userDetails.getUsername());
        String refreshToken = refreshTokenService.createRefreshToken(userId);

        return new ResponseEntity<>(Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        ), HttpStatus.OK);

    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        String oldRefresh = request.get("refreshToken");

        if (oldRefresh == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Missing refresh token"));
        }

        try {
            RefreshTokenService.TokenRotationResult rotationResult = refreshTokenService.validateAndRotate(oldRefresh);

            String newAccessToken = jwtUtil.generateAccessToken(
                    userServiceImpl.getUsernameById(rotationResult.userId())
            );

            return ResponseEntity.ok(Map.of(
                    "accessToken", newAccessToken,
                    "refreshToken", rotationResult.newPlainToken() // Use the new token from the result
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Logout - revoke refresh token
     */
    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (refreshToken != null) {
            boolean isRevoked = refreshTokenService.revoke(refreshToken);

            if (isRevoked) {
                return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Failed to revoke refresh token"));
            }
        }
        return ResponseEntity.badRequest().body(Map.of("error", "No refresh token provided"));
    }


    @DeleteMapping
    public ResponseEntity<String> deleteUser(@RequestBody UserRequestDto deleteRequest) {
        userServiceImpl.deleteUser(deleteRequest.getUserName(), deleteRequest.getPassword());
        return new ResponseEntity<>("User deleted", HttpStatus.OK);
    }

}
