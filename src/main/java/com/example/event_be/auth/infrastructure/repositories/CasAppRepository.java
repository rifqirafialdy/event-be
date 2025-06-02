package com.example.event_be.auth.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.event_be.auth.domain.entities.CasApp;

import java.util.Optional;

public interface CasAppRepository extends JpaRepository<CasApp, String> {
    Optional<CasApp> findByReferenceNumber(String referenceNumber);
}
