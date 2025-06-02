package com.example.event_be.auth.infrastructure.repositories;

import com.example.event_be.auth.domain.entities.SysScreen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysScreenRepository extends JpaRepository<SysScreen, String> {
}
