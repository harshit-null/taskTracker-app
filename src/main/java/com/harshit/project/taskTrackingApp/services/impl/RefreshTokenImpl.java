package com.harshit.project.taskTrackingApp.services.impl;

import com.github.f4b6a3.ulid.UlidCreator;
import com.harshit.project.taskTrackingApp.entities.RefreshToken;
import com.harshit.project.taskTrackingApp.repository.RefreshTokenRepository;
import com.harshit.project.taskTrackingApp.services.RefreshTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@Service
public class RefreshTokenImpl implements RefreshTokenService {

    private final RefreshTokenRepository repo;
    private final SecureRandom secureRandom = new SecureRandom();
    private final long refreshExpiryDays = 30; // configurable

    public RefreshTokenImpl(RefreshTokenRepository refreshTokenRepository) {
        this.repo = refreshTokenRepository;
    }

    @Override
    public String createRefreshToken(UUID userId) {
        String plain = generateRandomToken();
        String hash = sha56(plain);

        RefreshToken t = new RefreshToken();
        t.setId(UlidCreator.getUlid().toString());
        t.setUserId(userId);
        t.setTokenHash(hash);
        t.setCreatedAt(Instant.now());
        t.setExpiresAt(Instant.now().plusSeconds(refreshExpiryDays * 24 * 3600));

        repo.save(t);
        return plain;
    }

    @Override
    @Transactional
    public TokenRotationResult validateAndRotate(String plainToken) {
      String hash = sha56(plainToken);
        RefreshToken oldToken = repo.findByTokenHash(hash)
                .orElseThrow(() -> new RuntimeException("Invalid refresh Token"));

        if (oldToken.getExpiresAt().isBefore(Instant.now())) {
            deleteToken(plainToken);
            throw new RuntimeException("Refresh Token Expired");
        }
        repo.deleteByTokenHash(hash);

        String newPlain = generateRandomToken();
        String newHash = sha56(newPlain);

        RefreshToken newToken = new RefreshToken();
        newToken.setId(UlidCreator.getUlid().toString());
        newToken.setUserId(oldToken.getUserId());
        newToken.setTokenHash(newHash);
        newToken.setCreatedAt(Instant.now());
        newToken.setExpiresAt(Instant.now().plusSeconds(refreshExpiryDays * 24 * 3600));

        repo.save(newToken);

        return new TokenRotationResult(newPlain, oldToken.getUserId());
    }

    @Override
    @Transactional
    public boolean revoke(String plainToken) {

        String hash = sha56(plainToken);
        repo.deleteByTokenHash(hash);
        repo.flush();

        boolean exists = repo.findByTokenHash(hash).isPresent();

        return !exists;
    }

    @Override
    public String generateRandomToken() {
        byte[] b = new byte[48];
        secureRandom.nextBytes(b);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(b);
    }

    @Override
    public String sha56(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte by : digest) {
                sb.append(String.format("%02x", by));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void deleteToken(String plainToken) {
        String hash = sha56(plainToken);
        repo.deleteByTokenHash(hash);
    }
}