package com.example.event_be.event.infrastructure.repositories;

import com.example.event_be.event.domain.entities.EvtAppSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvtAppScheduleRepository extends JpaRepository<EvtAppSchedule, String> {
    List<EvtAppSchedule> findByEvtAppCountryId(String evtAppCountryId);
}
