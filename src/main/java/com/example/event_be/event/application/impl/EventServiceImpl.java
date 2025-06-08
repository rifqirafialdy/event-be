package com.example.event_be.event.application.impl;

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
        // 1. Save evt_app
        String evtAppId = UUID.randomUUID().toString();
        EvtApp evtApp = EvtApp.builder()
                .id(evtAppId)
                .name(request.getName())
                .referenceNo(request.getReferenceNo())
                .ectAppCode(request.getEctAppCode())
                .build();
        evtApp = evtAppRepository.save(evtApp);

        // 2. Save evt_app_country
        String countryCode = request.getSchedules().get(0).getParCountryCode();
        String evtAppCountryId = UUID.randomUUID().toString();
        EvtAppCountry evtAppCountry = EvtAppCountry.builder()
                .id(evtAppCountryId)
                .evtApp(evtApp) // directly set object
                .parCountryCode(countryCode)
                .build();
        evtAppCountry = evtAppCountryRepository.save(evtAppCountry);

        // 3. Save schedules and tickets
        for (EventCreateRequest.ScheduleDTO scheduleDTO : request.getSchedules()) {
            String scheduleId = UUID.randomUUID().toString();
            EvtAppSchedule schedule = EvtAppSchedule.builder()
                    .id(scheduleId)
                    .evtAppCountry(evtAppCountry) // set object
                    .parCityCode(scheduleDTO.getParCityCode())
                    .evtParChannelTypeCode(scheduleDTO.getChannelCode())
                    .evtDateStart(scheduleDTO.getDateStart())
                    .evtDateEnd(scheduleDTO.getDateEnd())
                    .addressLine1(scheduleDTO.getAddress1())
                    .addressLine2(scheduleDTO.getAddress2())
                    .addressLine3(scheduleDTO.getAddress3())
                    .build();
            schedule = evtAppScheduleRepository.save(schedule);

            for (EventCreateRequest.TicketDTO ticketDTO : request.getTickets()) {
                String ticketId = UUID.randomUUID().toString();
                EvtAppTicket ticket = EvtAppTicket.builder()
                        .id(ticketId)
                        .evtAppSchedule(schedule) // set object instead of scheduleId
                        .parTicketCategoryCode(ticketDTO.getCategoryCode())
                        .parTicketCategoryName(ticketDTO.getCategoryName())
                        .parTicketCategoryCapacity(ticketDTO.getCapacity())
                        .parTicketCategoryPrice(ticketDTO.getPrice())
                        .build();
                evtAppTicketRepository.save(ticket);
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
