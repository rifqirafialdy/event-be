package com.example.event_be.auth.presentation.controllers;

import com.example.event_be.auth.application.services.OrganizerProfileService;
import com.example.event_be.auth.domain.entities.OrganizerProfile;
import com.example.event_be.auth.presentation.DTO.OrganizerProfileResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/organizer/profile")
@RequiredArgsConstructor
public class OrganizerProfileController {

    private final OrganizerProfileService organizerProfileService;

    @PreAuthorize("@rbacService.hasAccess(authentication.name, 'PROFILE', 'CREATE')")
    @PostMapping
    public ResponseEntity<?> saveProfile(@RequestBody @Valid OrganizerProfile request, Authentication authentication) {
        String userId = authentication.getName();
        OrganizerProfile saved = organizerProfileService.saveOrUpdateProfile(userId, request);
        return ResponseEntity.ok(saved);
    }

    @PreAuthorize("@rbacService.hasAccess(authentication.name, 'PROFILE', 'VIEW')")
    @GetMapping
    public ResponseEntity<OrganizerProfileResponse> getProfile(Authentication authentication) {
        String userId = authentication.getName();
        OrganizerProfileResponse profile = organizerProfileService.getProfileDetails(userId);
        return ResponseEntity.ok(profile);
    }

    // ✅ Move this inside the class
    @PreAuthorize("@rbacService.hasAccess(authentication.name, 'PROFILE', 'VIEW')")
    @GetMapping("/exists")
    public ResponseEntity<?> checkProfileExists(Authentication authentication) {
        String userId = authentication.getName();
        boolean exists = organizerProfileService.existsByUserId(userId);
        return ResponseEntity.ok(Map.of("hasProfile", exists));
    }
}
