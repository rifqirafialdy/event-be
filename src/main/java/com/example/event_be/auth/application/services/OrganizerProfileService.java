package com.example.event_be.auth.application.services;

import com.example.event_be.auth.domain.entities.OrganizerProfile;
import com.example.event_be.auth.presentation.DTO.OrganizerProfileResponse;

public interface OrganizerProfileService {
    OrganizerProfile saveOrUpdateProfile(String email, OrganizerProfile request);
    OrganizerProfileResponse getProfileDetails(String userId);


    boolean existsByUserId(String userId);
}
