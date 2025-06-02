package com.example.event_be.auth.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.event_be.auth.domain.entities.BnsAppTran;

public interface BnsAppTranRepository extends JpaRepository<BnsAppTran, String> {
}
