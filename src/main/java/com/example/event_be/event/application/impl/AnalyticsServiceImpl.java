package com.example.event_be.event.application.impl;

import com.example.event_be.event.application.services.AnalyticsService;
import com.example.event_be.event.infrastructure.repositories.EvtAppRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final EvtAppRepository evtAppRepository;

    @Override
    public Map<LocalDate, Long> getDailyTicketSales(String organizerId, LocalDate start, LocalDate end) {
        return evtAppRepository.findAllByCreatedBy(organizerId).stream()
                .flatMap(evt -> evt.getTickets().stream())
                .flatMap(ticket -> ticket.getOwners().stream())
                .filter(owner -> {
                    LocalDate date = owner.getCreatedAt().toLocalDate();
                    return !date.isBefore(start) && !date.isAfter(end);
                })
                .collect(Collectors.groupingBy(
                        owner -> owner.getCreatedAt().toLocalDate(),
                        Collectors.counting()
                ));
    }
}
