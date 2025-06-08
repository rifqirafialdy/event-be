package com.example.event_be.event.application.services;

import java.util.List;
import java.util.Map;

public interface AnalyticsService {
    Map<String, Object> getSummary(String organizerId);
    List<Map<String, Object>> getDailySales(String organizerId);
    List<Map<String, Object>> getRevenueByEvent(String organizerId);
    Map<String, Long> getStatusBreakdown(String organizerId);
}
