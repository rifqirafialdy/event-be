package com.example.event_be.auth.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "referral_points", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "source_user_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReferralPoints {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private SysUser user;

    @Column(nullable = false)
    private int points;

    @ManyToOne
    @JoinColumn(name = "source_user_id")
    private SysUser sourceUser;

    @Column(nullable = false)
    private ZonedDateTime expiresAt;

    @CreationTimestamp
    private ZonedDateTime createdAt;
}
