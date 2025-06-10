package com.example.event_be.transaction.application.services;

import com.example.event_be.transaction.presentation.DTO.TicketPurchaseRequest;

public interface TransactionService {
    void purchaseTicket(TicketPurchaseRequest request, String userId);
    boolean checkDiscountEligibility(String userId);

}
