package com.example.event_be.event.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "evt_app_ticket")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvtAppTicket {

    @Id
    @Column(length = 50)
    private String id = UUID.randomUUID().toString();

    @Column(length = 50)
    private String evtAppScheduleId;

    @Column(length = 20)
    private String parTicketCategoryCode;

    @Column(length = 50)
    private String parTicketCategoryName;

    private int parTicketCategoryCapacity = 1;

    private double parTicketCategoryPrice = 0;

    private boolean dicounted = false;
    private boolean percented = false;

    private double parTicketCategoryDiscountPrice = 0;

    private boolean discountValid = false;

    private ZonedDateTime discountValidStartDate = ZonedDateTime.now();
    private ZonedDateTime discountValidEndDate = ZonedDateTime.now();

    @Column(length = 50)
    private String createdBy;

    @CreationTimestamp
    private ZonedDateTime createdAt;

    @Column(length = 50)
    private String updatedBy;

    @UpdateTimestamp
    private ZonedDateTime updatedAt;

    private boolean approved = false;
    private boolean deleted = false;
    private boolean published = false;

    private String approvedBy;

    private ZonedDateTime approvedAt;

    private int version = 1;
}
