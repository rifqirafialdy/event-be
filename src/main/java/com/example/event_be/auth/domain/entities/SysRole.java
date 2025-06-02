package com.example.event_be.auth.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "sys_role")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysRole {

    @Id
    @Column(length = 20)
    private String code;

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

    @OneToMany(mappedBy = "sysRole")
    private List<SysScreenRole> screenRoles;

    @OneToMany(mappedBy = "sysRole")
    private List<SysUserRole> userRoles;

}
