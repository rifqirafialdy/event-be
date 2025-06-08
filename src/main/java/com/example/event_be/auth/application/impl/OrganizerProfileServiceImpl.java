package com.example.event_be.auth.application.impl;

import com.example.event_be.auth.application.services.OrganizerProfileService;
import com.example.event_be.auth.domain.entities.OrganizerProfile;
import com.example.event_be.auth.domain.entities.SysUser;
import com.example.event_be.auth.infrastructure.repositories.OrganizerProfileRepository;
import com.example.event_be.auth.infrastructure.repositories.SysUserRepository;
import com.example.event_be.auth.presentation.DTO.OrganizerProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizerProfileServiceImpl implements OrganizerProfileService {

    private final OrganizerProfileRepository organizerProfileRepository;
    private final SysUserRepository sysUserRepository;

    @Override
    public OrganizerProfile saveOrUpdateProfile(String userId, OrganizerProfile request) {
        SysUser user = sysUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<OrganizerProfile> existing = organizerProfileRepository.findBySysUserId(user.getId());

        OrganizerProfile profile;
        if (existing.isPresent()) {
            profile = existing.get();
            profile.setName(request.getName());
            profile.setProfilePicture(request.getProfilePicture());
            profile.setDescription(request.getDescription());
            profile.setUpdatedAt(ZonedDateTime.now());
        } else {
            profile = OrganizerProfile.builder()
                    .id(UUID.randomUUID())
                    .sysUser(user)
                    .name(request.getName())
                    .profilePicture(request.getProfilePicture())
                    .description(request.getDescription())
                    .createdAt(ZonedDateTime.now())
                    .build();
        }

        return organizerProfileRepository.save(profile);
    }

    @Override
    public OrganizerProfileResponse getProfileDetails(String userId) {
        SysUser user = sysUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        OrganizerProfile profile = organizerProfileRepository.findBySysUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        return OrganizerProfileResponse.builder()
                .id(profile.getId().toString())
                .name(profile.getName())
                .profilePicture(profile.getProfilePicture())
                .description(profile.getDescription())
                .build();
    }


    @Override
    public boolean existsByUserId(String userId) {
        return organizerProfileRepository.existsBySysUserId(userId);
    }

}
