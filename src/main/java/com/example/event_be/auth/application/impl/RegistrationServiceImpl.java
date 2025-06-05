package com.example.event_be.auth.application.impl;

import com.example.event_be.auth.application.services.RegistrationService;
import com.example.event_be.auth.domain.entities.*;
import com.example.event_be.auth.infrastructure.repositories.*;
import com.example.event_be.auth.presentation.DTO.Registration.RegisterUserRequest;
import com.example.event_be.refferal.domain.entities.*;
import com.example.event_be.refferal.infrastructure.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final SysUserRepository sysUserRepository;
    private final SysRoleRepository sysRoleRepository;
    private final SysUserRoleRepository sysUserRoleRepository;
    private final CasAppRepository casAppRepository;
    private final CasAppBnsRepository casAppBnsRepository;
    private final BnsAppTranRepository bnsAppTranRepository;
    private final CasAppWalletRepository casAppWalletRepository;
    private final CasAppWalletTranRepository casAppWalletTranRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void registerUser(RegisterUserRequest request) {
        if (sysUserRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }
        String userId = UUID.randomUUID().toString();
        String referralCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        SysUser user = SysUser.builder()
                .id(userId)
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .createdBy("SYSTEM")
                .createdAt(ZonedDateTime.now())
                .build();

        sysUserRepository.save(user);

        SysUserRole userRole = SysUserRole.builder()
                .id(UUID.randomUUID().toString())
                .sysUser(user)
                .sysRole(sysRoleRepository.findById(request.getRole().toUpperCase()).orElseThrow())
                .createdBy("SYSTEM")
                .createdAt(ZonedDateTime.now())
                .build();

        sysUserRoleRepository.save(userRole);

        boolean isCustomer = request.getRole().equalsIgnoreCase("CUSTOMER");

        if (!isCustomer) return;

        CasApp casApp = CasApp.builder()
                .id(UUID.randomUUID().toString())
                .appUser(user)
                .referenceNumber(referralCode)
                .createdBy("SYSTEM")
                .createdAt(ZonedDateTime.now())
                .build();

        if (request.getReferredBy() != null && !request.getReferredBy().isEmpty()) {
            if (request.getReferredBy().equalsIgnoreCase(referralCode)) {
                throw new IllegalArgumentException("You cannot use your own referral code.");
            }

            Optional<CasApp> referrerApp = casAppRepository.findByReferenceNumber(request.getReferredBy());

            if (referrerApp.isEmpty()) {
                throw new IllegalArgumentException("Invalid referral code.");
            }

            casApp.setPreReferenceNumber(request.getReferredBy());
            casAppRepository.save(casApp);

            Optional<BnsAppTran> bonus = bnsAppTranRepository.findAll().stream().findFirst();
            bonus.ifPresent(b -> {
                CasAppBns casAppBns = CasAppBns.builder()
                        .id(UUID.randomUUID().toString())
                        .casApp(casApp)
                        .bnsAppTran(b)
                        .applied(true)
                        .amount(b.getAmount())
                        .createdBy("SYSTEM")
                        .createdAt(ZonedDateTime.now())
                        .build();

                casAppBnsRepository.save(casAppBns);

                String referrerReferenceNumber = referrerApp.get().getReferenceNumber();

                CasAppWallet wallet = casAppWalletRepository
                        .findByCasAppId(referrerApp.get().getId())
                        .orElse(CasAppWallet.builder()
                                .id(UUID.randomUUID().toString())
                                .casAppId(referrerApp.get().getId())
                                .walletTypeCode("REFERRAL")
                                .currencyCode("IDR")
                                .referenceNumber(referrerReferenceNumber)
                                .walletAmount(BigDecimal.ZERO)
                                .createdBy("SYSTEM")
                                .createdAt(ZonedDateTime.now())
                                .build());

                wallet.setWalletAmount(wallet.getWalletAmount().add(BigDecimal.valueOf(10000)));
                wallet.setUpdatedBy("SYSTEM");
                wallet.setUpdatedAt(ZonedDateTime.now());

                casAppWalletRepository.save(wallet);

                CasAppWalletTran walletTran = CasAppWalletTran.builder()
                        .id(UUID.randomUUID().toString())
                        .casAppWalletId(wallet.getId())
                        .tranCode("REFERRAL_BONUS")
                        .bookTypeCode("CREDIT")
                        .toReferenceNumber(userId)
                        .amount(BigDecimal.valueOf(10000))
                        .amountExpired(false)
                        .amountExpiredAt(ZonedDateTime.now().plusMonths(3))
                        .build();

                casAppWalletTranRepository.save(walletTran);
            });

        } else {
            // no referral, just save casApp
            casAppRepository.save(casApp);
        }
    }
}
