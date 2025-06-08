package com.example.event_be.event.presentation.DTO;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class EventCreateRequest {
    private String name;
    private String referenceNo;
    private String ectAppCode; // this links to your master event config (ect_app)

    private List<ScheduleDTO> schedules;
    private List<TicketDTO> tickets;

    @Data
    public static class ScheduleDTO {
        private String parCountryCode; // used to create evt_app_country
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
