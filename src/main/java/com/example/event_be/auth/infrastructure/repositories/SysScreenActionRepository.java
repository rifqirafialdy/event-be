package com.example.event_be.auth.infrastructure.repositories;

import com.example.event_be.auth.domain.entities.SysScreenAction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysScreenActionRepository extends JpaRepository<SysScreenAction, String> {
}
