package com.example.event_be.auth.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.event_be.auth.domain.entities.CasAppBns;

public interface CasAppBnsRepository extends JpaRepository<CasAppBns, String> {
}
