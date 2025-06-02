package com.example.event_be.auth.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "cas_app")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CasApp {

    @Id
    @Column(length = 50)
    private String id;

    @ManyToOne
    @JoinColumn(name = "app_user_id")
    private SysUser appUser;

    @Column(length = 40, unique = true)
    private String referenceNumber;

    @Column(length = 40)
    private String preReferenceNumber;

    @Column(length = 50)
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
