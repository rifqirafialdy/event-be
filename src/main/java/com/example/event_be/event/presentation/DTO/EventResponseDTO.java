package com.example.event_be.event.presentation.DTO;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
public class EventResponseDTO {
    private String id;
    private String name;
    private String referenceNo;
    private String ectAppCode;
    private ZonedDateTime createdAt;
    private List<ScheduleDTO> schedules;

    @Data
    @Builder
    public static class ScheduleDTO {
        private String scheduleId;         // ← ADD THIS
        private String cityCode;
        private String channelCode;
        private ZonedDateTime startDate;
        private ZonedDateTime endDate;
        private String address1;
        private String address2;
        private String address3;
        private List<TicketDTO> tickets;
    }

    @Data
    @Builder
    public static class TicketDTO {
        private String ticketId;           // ← ADD THIS
        private String categoryCode;
        private String categoryName;
        private int capacity;
        private double price;
    }


}
