package com.example.event_be.event.domain.entities;

import com.fasterxml.jackson.core.JsonToken;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Table(name = "evt_app_ticket_owner")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvtAppTicketOwner {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evt_app_ticket_id")
    private EvtAppTicket evtAppTicket;

    @Column(name = "sys_user_id")
    private String userId;

    private String wfAppId;
    private String currWfParStateCode;
    private String referenceNumber;
    private String casAppId;

    private int quantity;
    private double totalPaid;

    private ZonedDateTime createdAt;


}


