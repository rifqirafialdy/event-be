package com.example.event_be.event.application.services.impl;

import com.example.event_be.event.application.services.EventService;
import com.example.event_be.event.domain.entities.*;
import com.example.event_be.event.infrastructure.repositories.*;
import com.example.event_be.event.presentation.DTO.EventCreateRequest;
import com.example.event_be.event.presentation.DTO.EventResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EvtAppRepository evtAppRepository;
    private final EvtAppCountryRepository evtAppCountryRepository;
    private final EvtAppScheduleRepository evtAppScheduleRepository;
    private final EvtAppTicketRepository evtAppTicketRepository;

    @Override
    @Transactional
    public void createEvent(EventCreateRequest request) {

        // 1. Save to evt_app
        String evtAppId = UUID.randomUUID().toString();
        System.out.println("[DEBUG] Creating evt_app with ID: " + evtAppId);

        EvtApp evtApp = EvtApp.builder()
                .id(evtAppId)
                .name(request.getName())
                .referenceNo(request.getReferenceNo())
                .ectAppCode(request.getEctAppCode())
                .build();

        evtApp = evtAppRepository.save(evtApp);

        // 2. Save to evt_app_country
        String countryCode = request.getSchedules().get(0).getParCountryCode();
        String evtAppCountryId = UUID.randomUUID().toString();
        System.out.println("[DEBUG] Creating evt_app_country with ID: " + evtAppCountryId +
                ", evt_app_id: " + evtAppId + ", country: " + countryCode);

        EvtAppCountry evtAppCountry = EvtAppCountry.builder()
                .id(evtAppCountryId)
                .evtAppId(evtAppId)
                .parCountryCode(countryCode)
                .build();

        evtAppCountry = evtAppCountryRepository.save(evtAppCountry);

        // 3. Save schedules and tickets
        for (EventCreateRequest.ScheduleDTO schedule : request.getSchedules()) {
            String scheduleId = UUID.randomUUID().toString();
            System.out.println("[DEBUG] Creating evt_app_schedule with ID: " + scheduleId +
                    ", evt_app_country_id: " + evtAppCountryId);

            EvtAppSchedule evtAppSchedule = EvtAppSchedule.builder()
                    .id(scheduleId)
                    .evtAppCountryId(evtAppCountryId)
                    .parCityCode(schedule.getParCityCode())
                    .evtParChannelTypeCode(schedule.getChannelCode())
                    .evtDateStart(schedule.getDateStart())
                    .evtDateEnd(schedule.getDateEnd())
                    .addressLine1(schedule.getAddress1())
                    .addressLine2(schedule.getAddress2())
                    .addressLine3(schedule.getAddress3())
                    .build();

            evtAppSchedule = evtAppScheduleRepository.save(evtAppSchedule);

            for (EventCreateRequest.TicketDTO ticket : request.getTickets()) {
                String ticketId = UUID.randomUUID().toString();
                System.out.println("[DEBUG] Creating evt_app_ticket with ID: " + ticketId +
                        ", schedule_id: " + scheduleId);

                EvtAppTicket evtAppTicket = EvtAppTicket.builder()
                        .id(ticketId)
                        .evtAppScheduleId(scheduleId)
                        .parTicketCategoryCode(ticket.getCategoryCode())
                        .parTicketCategoryName(ticket.getCategoryName())
                        .parTicketCategoryCapacity(ticket.getCapacity())
                        .parTicketCategoryPrice(ticket.getPrice())
                        .build();

                evtAppTicketRepository.save(evtAppTicket);
            }
        }
    }

    @Override
    public List<EventResponseDTO> getAllEvents() {
        List<EvtApp> events = evtAppRepository.findAll();

        return events.stream().map(event -> {
            List<EvtAppCountry> countries = evtAppCountryRepository.findByEvtAppId(event.getId());

            List<EventResponseDTO.ScheduleDTO> scheduleDTOs = countries.stream()
                    .flatMap(country -> evtAppScheduleRepository.findByEvtAppCountryId(country.getId()).stream()
                            .map(schedule -> {
                                List<EvtAppTicket> tickets = evtAppTicketRepository.findByEvtAppScheduleId(schedule.getId());

                                List<EventResponseDTO.TicketDTO> ticketDTOs = tickets.stream().map(ticket ->
                                        EventResponseDTO.TicketDTO.builder()
                                                .categoryCode(ticket.getParTicketCategoryCode())
                                                .categoryName(ticket.getParTicketCategoryName())
                                                .capacity(ticket.getParTicketCategoryCapacity())
                                                .price(ticket.getParTicketCategoryPrice())
                                                .build()
                                ).toList();

                                return EventResponseDTO.ScheduleDTO.builder()
                                        .cityCode(schedule.getParCityCode())
                                        .channelCode(schedule.getEvtParChannelTypeCode())
                                        .startDate(schedule.getEvtDateStart())
                                        .endDate(schedule.getEvtDateEnd())
                                        .address1(schedule.getAddressLine1())
                                        .address2(schedule.getAddressLine2())
                                        .address3(schedule.getAddressLine3())
                                        .tickets(ticketDTOs)
                                        .build();
                            })
                    ).toList();

            return EventResponseDTO.builder()
                    .id(event.getId())
                    .name(event.getName())
                    .referenceNo(event.getReferenceNo())
                    .ectAppCode(event.getEctAppCode())
                    .createdAt(event.getCreatedAt())
                    .schedules(scheduleDTOs)
                    .build();
        }).toList();
    }

}
