package com.example.event_be.event.presentation.DTO;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class EventCreateRequest {
    private String name;
    private String referenceNo;
    private String ectAppCode;
    private List<ScheduleDTO> schedules;
    private List<TicketDTO> tickets;
    private String organizerId;


    @Data
    public static class ScheduleDTO {
        private String parCountryCode;
        private String parCityCode;
        private String channelCode;
        private ZonedDateTime dateStart;
        private ZonedDateTime dateEnd;
        private String address1;
        private String address2;
        private String address3;
    }

    @Data
    public static class TicketDTO {
        private String categoryCode;
        private String categoryName;
        private int capacity;
        private long price;
    }
}
