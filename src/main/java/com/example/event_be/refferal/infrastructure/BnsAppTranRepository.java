package com.example.event_be.refferal.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.event_be.refferal.domain.entities.BnsAppTran;

public interface BnsAppTranRepository extends JpaRepository<BnsAppTran, String> {
}
