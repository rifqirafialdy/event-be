package com.example.event_be.transaction.application.impl;

import com.example.event_be.transaction.application.services.TransactionService;
import com.example.event_be.transaction.infrastructure.repositories.EvtAppTicketOwnerRepository;
import com.example.event_be.transaction.presentation.DTO.TicketPurchaseRequest;
import com.example.event_be.event.domain.entities.*;
import com.example.event_be.event.infrastructure.repositories.*;
import com.example.event_be.refferal.domain.entities.*;
import com.example.event_be.refferal.infrastructure.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final EvtAppScheduleRepository evtAppScheduleRepository;
    private final EvtAppTicketRepository evtAppTicketRepository;
    private final EvtAppTicketOwnerRepository evtAppTicketOwnerRepository;

    private final CasAppRepository casAppRepository;
    private final CasAppWalletRepository casAppWalletRepository;
    private final CasAppWalletTranRepository casAppWalletTranRepository;

    @Transactional
    @Override
    public void purchaseTicket(TicketPurchaseRequest request, String userId) {
        // 1. Validate ticket
        EvtAppTicket ticket = evtAppTicketRepository.findById(request.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (ticket.getParTicketCategoryCapacity() < request.getQuantity()) {
            throw new IllegalArgumentException("Not enough tickets available");
        }

        // 2. Get Customer CasApp
        CasApp casApp = casAppRepository.findByAppUserId(userId)
                .orElseThrow(() -> new RuntimeException("Customer CasApp not found"));

        BigDecimal price = BigDecimal.valueOf(ticket.getParTicketCategoryPrice());
        BigDecimal totalPrice = price.multiply(BigDecimal.valueOf(request.getQuantity()));


        BigDecimal discount = BigDecimal.ZERO;
        BigDecimal redeemPointsUsed = BigDecimal.ZERO;

        // 3. Apply referral 10% discount
        if (casApp.getPreReferenceNumber() != null && !casApp.getPreReferenceNumber().isEmpty()) {
            discount = totalPrice.multiply(BigDecimal.valueOf(0.10));
            totalPrice = totalPrice.subtract(discount);
        }

        // 4. Apply redeem points if enabled
        if (request.isUsePoints()) {
            CasAppWallet wallet = casAppWalletRepository.findByCasAppId(casApp.getId())
                    .orElseThrow(() -> new RuntimeException("Wallet not found"));

            BigDecimal available = wallet.getWalletAmount();
            if (available.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal toRedeem = totalPrice.min(available);
                redeemPointsUsed = toRedeem;
                totalPrice = totalPrice.subtract(toRedeem);

                // Deduct wallet amount
                wallet.setWalletAmount(available.subtract(toRedeem));
                wallet.setUpdatedAt(ZonedDateTime.now());
                wallet.setUpdatedBy(userId);
                casAppWalletRepository.save(wallet);

                // Create redeem transaction
                CasAppWalletTran redeemTran = CasAppWalletTran.builder()
                        .id(UUID.randomUUID().toString())
                        .casAppWalletId(wallet.getId())
                        .tranCode("REDEEM")
                        .bookTypeCode("DEBIT")
                        .toReferenceNumber(ticket.getId())
                        .amount(toRedeem)
                        .amountExpired(false)
                        .amountExpiredAt(ZonedDateTime.now().plusMonths(3))
                        .createdAt(ZonedDateTime.now())
                        .build();

                casAppWalletTranRepository.save(redeemTran);
            }
        }

        // 5. Save ticket ownership
        EvtAppTicketOwner owner = EvtAppTicketOwner.builder()
                .id(UUID.randomUUID().toString())
                .evtAppTicket(ticket)
                .userId(userId)
                .quantity(request.getQuantity())
                .totalPaid(totalPrice.doubleValue())
                .createdAt(ZonedDateTime.now())
                .build();

        evtAppTicketOwnerRepository.save(owner);

        // 6. Decrease ticket stock
        ticket.setParTicketCategoryCapacity(ticket.getParTicketCategoryCapacity() - request.getQuantity());
        evtAppTicketRepository.save(ticket);
    }
}
