package com.example.event_be.event.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;
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

    @Column(name = "address_line_1")
    private String addressLine1;

    @Column(name = "address_line_2")
    private String addressLine2;

    @Column(name = "address_line_3")
    private String addressLine3;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evt_app_country_id", insertable = false, updatable = false)
    private EvtAppCountry evtAppCountry;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "evt_app_schedule_id", referencedColumnName = "id", insertable = false, updatable = false)
    private List<EvtAppTicket> tickets;

}
