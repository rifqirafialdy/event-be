package com.example.event_be.auth.infrastructure.repositories;

import com.example.event_be.auth.domain.entities.CasAppWalletTran;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CasAppWalletTranRepository extends JpaRepository<CasAppWalletTran, String> {
}

