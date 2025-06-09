package com.example.event_be.event.presentation.controllers;

import com.example.event_be.event.application.services.EventService;
import com.example.event_be.event.presentation.DTO.EventCreateRequest;
import com.example.event_be.event.presentation.DTO.EventResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PreAuthorize("@rbacService.hasAccess(authentication.name, 'EVENTS', 'CREATE')")
    @PostMapping
    public ResponseEntity<String> createEvent(@RequestBody EventCreateRequest request, Authentication authentication ) {
        String organizerId = authentication.getName();
        eventService.createEvent(request, organizerId);
        return ResponseEntity.ok("Event created successfully");
    }

    // List all events with schedule and tickets
    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getAllEvents() {
        List<EventResponseDTO> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable String id) {
        EventResponseDTO event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }

}
