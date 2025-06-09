package com.example.event_be.event.application.services;

import com.example.event_be.event.presentation.DTO.EventCreateRequest;
import com.example.event_be.event.presentation.DTO.EventResponseDTO;

import java.util.List;

public interface EventService {
    void createEvent(EventCreateRequest request);
    List<EventResponseDTO> getAllEvents();
    EventResponseDTO getEventById(String id);

}
