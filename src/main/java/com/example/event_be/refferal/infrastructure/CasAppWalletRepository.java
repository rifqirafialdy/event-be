package com.example.event_be.refferal.infrastructure;

import com.example.event_be.refferal.domain.entities.CasAppWallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CasAppWalletRepository extends JpaRepository<CasAppWallet, String> {
    Optional<CasAppWallet> findByCasAppId(String casAppId);
}
