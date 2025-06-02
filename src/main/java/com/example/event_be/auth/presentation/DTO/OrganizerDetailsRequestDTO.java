package com.example.event_be.auth.presentation.DTO;

import lombok.Data;

@Data
public class OrganizerDetailsRequestDTO {
    private String organizationName;
    private String description;
    private String phoneNumber;
    private String address;
}
