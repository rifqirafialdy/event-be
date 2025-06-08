package com.example.event_be.event.presentation.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DailySalesDTO(
        long ticketsSold,
        BigDecimal totalRevenue,
        LocalDate date
) {}
