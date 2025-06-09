package com.example.event_be.transaction.infrastructure.repositories;

import com.example.event_be.event.domain.entities.EvtAppTicketOwner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvtAppTicketOwnerRepository extends JpaRepository<EvtAppTicketOwner, String> {
    // no need to manually declare save() — inherited from JpaRepository
}
