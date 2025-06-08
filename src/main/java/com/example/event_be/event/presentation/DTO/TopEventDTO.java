package com.example.event_be.event.presentation.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopEventDTO {
    private String eventName;
    private long totalSold;
}
