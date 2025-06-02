package com.example.event_be.auth.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.event_be.auth.domain.entities.SysUser;

import java.util.Optional;

public interface SysUserRepository extends JpaRepository<SysUser, String> {
    Optional<SysUser> findByEmail(String email);
    Optional<SysUser> findById(String id);
    boolean existsByEmail(String email);
// ✅ No need to define manually

}
