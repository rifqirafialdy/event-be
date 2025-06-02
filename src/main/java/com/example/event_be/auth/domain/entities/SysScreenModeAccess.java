package com.example.event_be.auth.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sys_screen_mode_access")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysScreenModeAccess {

    @Id
    @Column(length = 50)
    private String id;

    @ManyToOne
    @JoinColumn(name = "sys_screen_role_id")
    private SysScreenRole sysScreenRole;

    @ManyToOne
    @JoinColumn(name = "sys_screen_action_id")
    private SysScreenAction sysScreenAction;
}
