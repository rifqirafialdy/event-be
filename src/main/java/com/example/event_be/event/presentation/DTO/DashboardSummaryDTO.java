package com.example.event_be.event.presentation.DTO;

import com.example.event_be.event.presentation.DTO.TopEventDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardSummaryDTO {
    private long totalEvents;
    private long totalParticipants;
    private long totalRevenue;
    private List<TopEventDTO> topEvents;
}
