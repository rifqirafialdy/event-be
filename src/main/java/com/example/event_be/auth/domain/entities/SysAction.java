package com.example.event_be.auth.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sys_action")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysAction {

    @Id
    @Column(length = 20)
    private String code; // e.g. "VIEW", "EDIT"

    @Column(length = 50)
    private String name;
}
