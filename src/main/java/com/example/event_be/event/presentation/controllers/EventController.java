package com.example.event_be.event.presentation.controllers;

import com.example.event_be.event.application.services.EventService;
import com.example.event_be.event.presentation.DTO.EventCreateRequest;
import com.example.event_be.event.presentation.DTO.EventResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    // Create event
    @PostMapping
    public ResponseEntity<String> createEvent(@RequestBody EventCreateRequest request) {
        eventService.createEvent(request);
        return ResponseEntity.ok("Event created successfully");
    }

    // List all events with schedule and tickets
    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getAllEvents() {
        List<EventResponseDTO> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }
}
