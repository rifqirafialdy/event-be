package com.example.event_be.refferal.presentation.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class ReferralPointDTO {
    private BigDecimal amount;
    private boolean expired;
    private ZonedDateTime expiresAt;
}
