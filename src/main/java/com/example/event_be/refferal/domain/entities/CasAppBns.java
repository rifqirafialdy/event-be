package com.example.event_be.refferal.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "cas_app_bns")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CasAppBns {

    @Id
    @Column(length = 50)
    private String id;

    @ManyToOne
    @JoinColumn(name = "cas_app_id")
    private CasApp casApp;

    @ManyToOne
    @JoinColumn(name = "bns_app_tran_id")
    private BnsAppTran bnsAppTran;

    private Boolean applied;
    private Boolean percented;
    private Double amount;
    private Boolean amountLimited;
    private Double maximumAmount;

    private ZonedDateTime expiredAt;

    private String createdBy;
    private ZonedDateTime createdAt;
    private String updatedBy;
    private ZonedDateTime updatedAt;

    private Boolean approved = false;
    private Boolean deleted = false;
    private Boolean published = false;

    private String approvedBy;
    private ZonedDateTime approvedAt;

    private Integer version = 1;
}
