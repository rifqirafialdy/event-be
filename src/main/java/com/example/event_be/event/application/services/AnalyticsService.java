package com.example.event_be.event.application.services;

import java.time.LocalDate;
import java.util.Map;

public interface AnalyticsService {
    Map<LocalDate, Long> getDailyTicketSales(String organizerId, LocalDate start, LocalDate end);
}
