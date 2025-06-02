package com.example.event_be.auth.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sys_screen_action")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysScreenAction {

    @Id
    @Column(length = 50)
    private String id;

    @ManyToOne
    @JoinColumn(name = "sys_screen_code")
    private SysScreen sysScreen;

    @ManyToOne
    @JoinColumn(name = "sys_action_code")
    private SysAction sysAction;
}
