package com.example.event_be.event.infrastructure.repositories;

import com.example.event_be.event.domain.entities.EvtApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvtAppRepository extends JpaRepository<EvtApp, String> {
    List<EvtApp> findAllByCreatedBy(String createdBy);

}
