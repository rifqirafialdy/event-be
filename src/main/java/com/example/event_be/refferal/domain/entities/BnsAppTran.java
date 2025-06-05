package com.example.event_be.refferal.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "bns_app_tran")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BnsAppTran {

    @Id
    @Column(length = 50)
    private String id;

    @Column(length = 20)
    private String bnsAppCode;

    @Column(length = 20)
    private String parCountryCode;

    @Column(length = 20)
    private String parCurrencyCode;

    private Boolean percented;
    private Double amount;
    private Boolean amountLimited;
    private Double maximumAmount;
    private Boolean expired;

    private ZonedDateTime expiredAt;

    @Column(length = 20)
    private String bnsParValidCode;

    private Integer bnsParValidValue;

    private String createdBy;
    private ZonedDateTime createdAt;
    private String updatedBy;
    private ZonedDateTime updatedAt;

    private Boolean approved = false;
    private Boolean deleted = false;
    private Boolean published = false;

    private String approvedBy;
    private ZonedDateTime approvedAt;
}
