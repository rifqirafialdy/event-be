package com.example.event_be.auth.presentation.DTO;

import lombok.Data;

@Data
public class CreateOrganizerProfileRequest {
    private String name;
    private String profilePicture;
    private String description;
}
