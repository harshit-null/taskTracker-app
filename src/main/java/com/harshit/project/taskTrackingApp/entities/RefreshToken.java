package com.harshit.project.taskTrackingApp.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
@Data
public class RefreshToken {

    @Id
    private String id; // ULID

    @Column(nullable = false)
    private java.util.UUID userId; // foreign key to User.id

    @Column(nullable = false, unique = true)
    private String tokenHash; // sha256 of refresh token

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant expiresAt;

    @Column
    private Instant lastUsedAt;

}
