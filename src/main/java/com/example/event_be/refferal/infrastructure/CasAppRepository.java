package com.example.event_be.refferal.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.event_be.refferal.domain.entities.CasApp;

import java.util.Optional;

public interface CasAppRepository extends JpaRepository<CasApp, String> {
    Optional<CasApp> findByReferenceNumber(String referenceNumber);
    Optional<CasApp> findByAppUserId(String userId);

}
