package com.example.event_be.auth.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "sys_screen_role")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysScreenRole {

    @Id
    @Column(length = 50)
    private String id;

    @ManyToOne
    @JoinColumn(name = "sys_screen_code")
    private SysScreen sysScreen;

    @ManyToOne
    @JoinColumn(name = "sys_role_code")
    private SysRole sysRole;

    @OneToMany(mappedBy = "sysScreenRole")
    private List<SysScreenModeAccess> modeAccesses;

}
