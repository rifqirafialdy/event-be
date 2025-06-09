package com.example.event_be.transaction.presentation.DTO;

import lombok.Data;

@Data
public class TicketPurchaseRequest {
    private String scheduleId;
    private String ticketId;
    private int quantity;
    private boolean usePoints; // if customer wants to redeem wallet points
}
