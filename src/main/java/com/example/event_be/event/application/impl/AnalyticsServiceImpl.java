package com.example.event_be.event.application.impl;

import com.example.event_be.event.application.services.AnalyticsService;
import com.example.event_be.event.infrastructure.repositories.EvtAppCountryRepository;
import com.example.event_be.event.infrastructure.repositories.EvtAppRepository;
import com.example.event_be.event.infrastructure.repositories.EvtAppScheduleRepository;
import com.example.event_be.event.infrastructure.repositories.EvtAppTicketRepository;
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
    private final EvtAppCountryRepository evtAppCountryRepository;
    private final EvtAppScheduleRepository evtAppScheduleRepository;
    private final EvtAppTicketRepository evtAppTicketRepository;


    @Override
    public Map<LocalDate, Long> getDailyTicketSales(String organizerId, LocalDate start, LocalDate end) {
        return evtAppRepository.findAllByCreatedBy(organizerId).stream()
                .flatMap(evt -> evtAppCountryRepository.findByEvtAppId(evt.getId()).stream())
                .flatMap(country -> evtAppScheduleRepository.findByEvtAppCountryId(country.getId()).stream())
                .flatMap(schedule -> evtAppTicketRepository.findByEvtAppScheduleId(schedule.getId()).stream())
                .flatMap(ticket -> {
                    if (ticket.getOwners() != null) {
                        return ticket.getOwners().stream();
                    } else {
                        return java.util.stream.Stream.empty();
                    }
                })
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
