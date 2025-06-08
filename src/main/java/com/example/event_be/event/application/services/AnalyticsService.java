package com.example.event_be.event.application.services;

import com.example.event_be.event.presentation.DTO.DashboardSummaryDTO;

import java.time.LocalDate;
import java.util.Map;

public interface AnalyticsService {
    Map<LocalDate, Long> getDailyTicketSales(String organizerId, LocalDate start, LocalDate end);
    DashboardSummaryDTO getDashboardSummary(String organizerId);

}
