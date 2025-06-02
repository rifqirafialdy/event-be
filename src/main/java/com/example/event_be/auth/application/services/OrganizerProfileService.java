package com.example.event_be.auth.application.services;

import com.example.event_be.auth.domain.entities.OrganizerProfile;

public interface OrganizerProfileService {
    OrganizerProfile saveOrUpdateProfile(String email, OrganizerProfile request);
    OrganizerProfile getProfileByEmail(String email);
}
