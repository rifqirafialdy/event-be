package com.example.event_be.event.application.impl;

import com.example.event_be.event.application.services.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Map<String, Object> getSummary(String organizerId) {
        return Map.of();
    }

    @Override
    public List<Map<String, Object>> getDailySales(String organizerId) {
        String sql = """
            SELECT DATE(o.created_at) AS date, COUNT(*) AS total
            FROM evt_app_ticket_owner o
            JOIN evt_app_ticket t ON o.evt_app_ticket_id = t.id
            JOIN evt_app_schedule s ON t.evt_app_schedule_id = s.id
            JOIN evt_app_country c ON s.evt_app_country_id = c.id
            JOIN evt_app e ON c.evt_app_id = e.id
            WHERE e.created_by = ?
            GROUP BY DATE(o.created_at)
            ORDER BY date;
        """;

        return jdbcTemplate.queryForList(sql, organizerId);
    }

    @Override
    public List<Map<String, Object>> getRevenueByEvent(String organizerId) {
        return List.of();
    }

    @Override
    public Map<String, Long> getStatusBreakdown(String organizerId) {
        return Map.of();
    }

    // You can add getSummary(), getRevenueByEvent(), getStatusBreakdown() here next.
}
