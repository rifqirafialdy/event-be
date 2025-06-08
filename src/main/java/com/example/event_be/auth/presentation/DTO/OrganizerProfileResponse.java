package com.example.event_be.auth.presentation.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizerProfileResponse {
    private String id;
    private String name;
    private String profilePicture;
    private String description;
}
