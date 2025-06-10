package com.example.event_be.transaction.presentation.controllers;

import com.example.event_be.transaction.application.services.TransactionService;
import com.example.event_be.transaction.presentation.DTO.TicketPurchaseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    @PreAuthorize("@rbacService.hasAccess(authentication.name, 'TICKETS', 'BUY')")
    @PostMapping("/purchase")
    public ResponseEntity<String> purchaseTicket(@RequestBody TicketPurchaseRequest request,
                                                 Authentication authentication) {
        String userId = authentication.getName(); // taken from JWT Principal (should match sys_user.id)
        transactionService.purchaseTicket(request, userId);
        return ResponseEntity.ok("Ticket purchased successfully");
    }

    @GetMapping("/check-discount-eligibility")
    public ResponseEntity<Map<String, Boolean>> checkDiscountEligibility(Principal principal) {
        String userId = principal.getName();
        boolean eligible = transactionService.checkDiscountEligibility(userId);
        return ResponseEntity.ok(Map.of("discountEligible", eligible));
    }
}
