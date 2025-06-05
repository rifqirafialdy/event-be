package com.example.event_be.refferal.presentation.controllers;

import com.example.event_be.refferal.application.services.ReferralService;
import com.example.event_be.refferal.presentation.DTO.ReferralInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/referral")
@RequiredArgsConstructor
public class ReferralController {

    private final ReferralService referralService;

    @PreAuthorize("@rbacService.hasAccess(authentication.name, 'REFERRAL', 'VIEW')")
    @GetMapping
    public ResponseEntity<ReferralInfoDTO> getReferralInfo(Authentication authentication) {
        String userId = authentication.getName();
        ReferralInfoDTO referralInfo = referralService.getReferralInfo(userId);
        return ResponseEntity.ok(referralInfo);
    }
}
