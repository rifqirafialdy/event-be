package com.example.event_be.auth.infrastructure.repositories;

import com.example.event_be.auth.domain.entities.OrganizerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrganizerProfileRepository extends JpaRepository<OrganizerProfile, UUID> {
    Optional<OrganizerProfile> findBySysUserId(String sysUserId);

    boolean existsBySysUserId(String userId);
}
