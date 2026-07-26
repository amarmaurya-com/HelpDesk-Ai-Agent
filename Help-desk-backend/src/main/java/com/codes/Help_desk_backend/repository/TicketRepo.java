package com.codes.Help_desk_backend.repository;

import com.codes.Help_desk_backend.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepo extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByEmail(String username);
    void deleteByEmail(String username);
}