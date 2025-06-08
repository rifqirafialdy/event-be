package com.example.event_be.event.presentation.controllers;

import com.example.event_be.event.application.services.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/organizer/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/sales-daily")
    public ResponseEntity<?> getDailySales(@RequestParam String organizerId) {
        return ResponseEntity.ok(analyticsService.getDailySales(organizerId));
    }

    // Add other endpoints here (summary, revenue, etc.)
}
