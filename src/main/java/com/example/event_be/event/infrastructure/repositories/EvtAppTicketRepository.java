package com.example.event_be.event.infrastructure.repositories;

import com.example.event_be.event.domain.entities.EvtAppTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvtAppTicketRepository extends JpaRepository<EvtAppTicket, String> {
    List<EvtAppTicket> findByEvtAppScheduleId(String evtAppScheduleId);


}
