package com.example.event_be.event.application.impl;

import com.example.event_be.event.application.services.AnalyticsService;
import com.example.event_be.event.infrastructure.repositories.EvtAppCountryRepository;
import com.example.event_be.event.infrastructure.repositories.EvtAppRepository;
import com.example.event_be.event.infrastructure.repositories.EvtAppScheduleRepository;
import com.example.event_be.event.infrastructure.repositories.EvtAppTicketRepository;
import com.example.event_be.event.presentation.DTO.DashboardSummaryDTO;
import com.example.event_be.event.presentation.DTO.TopEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                .flatMap(ticket -> ticket.getOwners() != null ? ticket.getOwners().stream() : Stream.empty())
                .filter(owner -> {
                    LocalDate date = owner.getCreatedAt().toLocalDate();
                    return !date.isBefore(start) && !date.isAfter(end);
                })
                .collect(Collectors.groupingBy(
                        owner -> owner.getCreatedAt().toLocalDate(),
                        Collectors.summingLong(owner -> owner.getQuantity()) // use quantity
                ));
    }

    @Override
    public DashboardSummaryDTO getDashboardSummary(String organizerId) {
        var events = evtAppRepository.findAllByCreatedBy(organizerId);

        long totalEvents = events.size();
        long totalParticipants = 0;
        long totalRevenue = 0;

        Map<String, Long> eventSalesMap = new HashMap<>();

        for (var evt : events) {
            var countries = evtAppCountryRepository.findByEvtAppId(evt.getId());
            for (var country : countries) {
                var schedules = evtAppScheduleRepository.findByEvtAppCountryId(country.getId());
                for (var schedule : schedules) {
                    var tickets = evtAppTicketRepository.findByEvtAppScheduleId(schedule.getId());
                    for (var ticket : tickets) {
                        long sold = 0;
                        long revenue = 0;

                        if (ticket.getOwners() != null) {
                            for (var owner : ticket.getOwners()) {
                                sold += owner.getQuantity();
                                revenue += (long) owner.getQuantity() * ticket.getParTicketCategoryPrice();
                            }
                        }

                        totalParticipants += sold;
                        totalRevenue += revenue;

                        eventSalesMap.merge(evt.getName(), sold, Long::sum);
                    }
                }
            }
        }

        List<TopEventDTO> topEvents = eventSalesMap.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .limit(5)
                .map(e -> new TopEventDTO(e.getKey(), e.getValue()))
                .toList();

        return new DashboardSummaryDTO(totalEvents, totalParticipants, totalRevenue, topEvents);
    }
}
