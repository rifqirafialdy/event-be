package com.example.event_be.event.domain.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;

@Entity
@Table(name = "evt_app_ticket_owner")
@Data
public class EvtAppTicketOwner {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evt_app_ticket_id")
    private EvtAppTicket evtAppTicket;

    @Column(name = "sys_user_id")
    private String userId; // buyer (customer)

    private String wfAppId;

    private String currWfParStateCode;

    private String referenceNumber;

    private String casAppId;

    private ZonedDateTime createdAt;
}
