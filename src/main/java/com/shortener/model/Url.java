package com.shortener.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="URLS",
        indexes = {
        @Index(name = "idx_short_id", columnList = "shortId")
        })
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 2048)
    private String url;

    @Column(unique = true,nullable = false,length = 10)
    private String shortId;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant expiresAt;

    @Column(nullable = false)
    private Long clickCount=0L;

    @Column(nullable = false)
    private boolean isActive=true;
}
