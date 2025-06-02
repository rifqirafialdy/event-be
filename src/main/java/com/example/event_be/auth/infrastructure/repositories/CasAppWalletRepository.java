package com.example.event_be.auth.infrastructure.repositories;

import com.example.event_be.auth.domain.entities.CasAppWallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CasAppWalletRepository extends JpaRepository<CasAppWallet, String> {
    Optional<CasAppWallet> findByCasAppId(String casAppId);
}
