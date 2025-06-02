package com.example.event_be.auth.infrastructure.repositories;

import com.example.event_be.auth.domain.entities.SysUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SysUserRoleRepository extends JpaRepository<SysUserRole, String> {
    List<SysUserRole> findBySysUserId(String userId);
}
