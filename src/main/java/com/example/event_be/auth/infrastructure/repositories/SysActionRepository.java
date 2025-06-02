package com.example.event_be.auth.infrastructure.repositories;

import com.example.event_be.auth.domain.entities.SysAction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysActionRepository extends JpaRepository<SysAction, String> {
}
