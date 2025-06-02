package com.example.event_be.auth.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "sys_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysUser {

    @Id
    @Column(length = 50)
    private String id;

    @Column(length = 150)
    private String name;

    @Column(length = 150, unique = true)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 50)
    private String firstName;

    @Column(length = 50)
    private String middleName;

    @Column(length = 50)
    private String lastName;

    @Column(length = 50)
    private String createdBy;

    @Column
    private ZonedDateTime createdAt;

    @Column(length = 50)
    private String updatedBy;

    @Column
    private ZonedDateTime updatedAt;

    private Boolean approved = false;
    private Boolean deleted = false;
    private Boolean published = false;

    private String approvedBy;
    private ZonedDateTime approvedAt;

    private Integer version = 1;

    @OneToMany(mappedBy = "sysUser", fetch = FetchType.EAGER)
    private List<SysUserRole> userRoles;

}
