package com.example.event_be.refferal.presentation.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ReferralInfoDTO {
    private String referralNumber;
    private BigDecimal walletAmount;
    private List<ReferralPointDTO> points;
}
