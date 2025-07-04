package com.example.event_be.event.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "evt_app_country")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvtAppCountry {

    @Id
    @Column(length = 50)
    private String id = UUID.randomUUID().toString();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evt_app_id")
    private EvtApp evtApp;

    @Column(length = 20)
    private String parCountryCode;

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

    @OneToMany(mappedBy = "evtAppCountry", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<EvtAppSchedule> schedules;
}
