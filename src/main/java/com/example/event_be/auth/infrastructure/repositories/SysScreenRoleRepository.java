package com.example.event_be.auth.infrastructure.repositories;

import com.example.event_be.auth.domain.entities.SysScreenRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysScreenRoleRepository extends JpaRepository<SysScreenRole, String> {
}
