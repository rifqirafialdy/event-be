package com.example.event_be.auth.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "organizer_profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizerProfile {
    @Id
    private UUID id;

    @OneToOne
    @JoinColumn(name = "sys_user_id", referencedColumnName = "id", unique = true)
    private SysUser sysUser;

    private String name;
    private String profilePicture;
    private String description;

    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}

