package com.example.event_be.refferal.application.impl;


import com.example.event_be.refferal.application.services.ReferralService;
import com.example.event_be.refferal.domain.entities.CasApp;
import com.example.event_be.refferal.domain.entities.CasAppWallet;
import com.example.event_be.refferal.infrastructure.CasAppRepository;
import com.example.event_be.refferal.infrastructure.CasAppWalletRepository;
import com.example.event_be.refferal.infrastructure.CasAppWalletTranRepository;
import com.example.event_be.refferal.presentation.DTO.ReferralInfoDTO;
import com.example.event_be.refferal.presentation.DTO.ReferralPointDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReferralServiceImpl implements ReferralService {

    private final CasAppRepository casAppRepository;
    private final CasAppWalletRepository casAppWalletRepository;
    private final CasAppWalletTranRepository casAppWalletTranRepository;

    @Override
    public ReferralInfoDTO getReferralInfo(String userId) {
        CasApp casApp = casAppRepository.findByAppUserId(userId)
                .orElseThrow(() -> new RuntimeException("Referral info not found"));

        CasAppWallet wallet = casAppWalletRepository.findByCasAppId(casApp.getId())
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        List<ReferralPointDTO> points = casAppWalletTranRepository.findByCasAppWalletId(wallet.getId())
                .stream()
                .map(tran -> new ReferralPointDTO(
                        tran.getAmount(),
                        tran.isAmountExpired(),
                        tran.getAmountExpiredAt()
                ))
                .collect(Collectors.toList());

        return new ReferralInfoDTO(
                casApp.getReferenceNumber(),
                wallet.getWalletAmount(),
                points
        );
    }
}
