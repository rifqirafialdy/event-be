package com.example.event_be.event.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "evt_app")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvtApp {

    @Id
    @Column(length = 50)
    private String id = UUID.randomUUID().toString();

    @Column(length = 20)
    private String ectAppCode;

    @Column(length = 40)
    private String referenceNo;

    @Column(length = 50)
    private String name;

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

    @OneToMany(mappedBy = "evtApp", fetch = FetchType.LAZY)
    private List<EvtAppTicket> tickets;
}
