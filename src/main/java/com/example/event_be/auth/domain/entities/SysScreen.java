package com.example.event_be.auth.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sys_screen")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysScreen {

    @Id
    @Column(length = 20)
    private String code; // e.g. "EVENTS", "DASHBOARD"

    @Column(length = 50)
    private String name;
}
