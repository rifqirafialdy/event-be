package com.example.event_be.event.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "evt_app_schedule")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvtAppSchedule {

    @Id
    @Column(length = 50)
    private String id = UUID.randomUUID().toString();

    @Column(length = 50)
    private String evtAppCountryId;

    @Column(length = 20)
    private String parCityCode;

    @Column(length = 20)
    private String evtParChannelTypeCode;

    private ZonedDateTime evtDateStart;

    private ZonedDateTime evtDateEnd;

    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
}
