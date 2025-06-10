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
        return casAppRepository.findByAppUserId(userId)
                .map(casApp -> {
                    CasAppWallet wallet = casAppWalletRepository.findByCasAppId(casApp.getId())
                            .orElse(null);

                    var walletAmount = wallet != null ? wallet.getWalletAmount() : java.math.BigDecimal.ZERO;

                    List<ReferralPointDTO> points = wallet != null
                            ? casAppWalletTranRepository.findByCasAppWalletId(wallet.getId()).stream()
                            .map(tran -> new ReferralPointDTO(
                                    tran.getAmount(),
                                    tran.isAmountExpired(),
                                    tran.getAmountExpiredAt(),
                                    tran.getBookTypeCode()
                            ))
                            .collect(Collectors.toList())
                            : List.of();

                    return new ReferralInfoDTO(
                            casApp.getReferenceNumber(),
                            walletAmount,
                            points
                    );
                })
                .orElseGet(() -> new ReferralInfoDTO(
                        null,
                        java.math.BigDecimal.ZERO,
                        List.of()
                ));
    }
}
