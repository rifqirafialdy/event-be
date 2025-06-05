package com.example.event_be.refferal.infrastructure;

import com.example.event_be.refferal.domain.entities.CasAppWalletTran;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CasAppWalletTranRepository extends JpaRepository<CasAppWalletTran, String> {
    List<CasAppWalletTran> findByCasAppWalletId(String casAppWalletId);
}

