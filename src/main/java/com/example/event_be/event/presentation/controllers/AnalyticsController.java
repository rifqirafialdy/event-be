package com.example.event_be.event.presentation.controllers;

import com.example.event_be.event.application.services.AnalyticsService;
import com.example.event_be.event.presentation.DTO.DashboardSummaryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/daily-sales")
    public ResponseEntity<Map<LocalDate, Long>> getDailySales(
            @RequestParam String organizerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        return ResponseEntity.ok(analyticsService.getDailyTicketSales(organizerId, start, end));
    }

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryDTO> getSummary(@RequestParam String organizerId) {
        return ResponseEntity.ok(analyticsService.getDashboardSummary(organizerId));
    }
}
