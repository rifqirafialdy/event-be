package com.example.event_be.auth.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "sys_user_role")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysUserRole {

    @Id
    @Column(length = 50)
    private String id;

    @ManyToOne
    @JoinColumn(name = "sys_user_id")
    private SysUser sysUser;

    @ManyToOne
    @JoinColumn(name = "sys_role_code")
    private SysRole sysRole;

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
