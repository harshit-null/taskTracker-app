package com.harshit.project.taskTrackingApp.services;

import java.util.UUID;

public interface RefreshTokenService {
    String createRefreshToken(UUID userId);
    TokenRotationResult validateAndRotate(String plainToken);
    boolean revoke(String plainToken);
    String generateRandomToken();
    String sha56(String input);
    void deleteToken(String plainToken);
    public record TokenRotationResult(String newPlainToken, UUID userId) {};
}
