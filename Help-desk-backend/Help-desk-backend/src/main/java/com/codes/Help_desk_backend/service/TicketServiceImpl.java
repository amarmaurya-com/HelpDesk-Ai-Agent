package com.codes.Help_desk_backend.service;

import com.codes.Help_desk_backend.Exceptions.ResourceNotFoundException;
import com.codes.Help_desk_backend.entity.Ticket;
import com.codes.Help_desk_backend.repository.TicketRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {

    private final TicketRepo ticketRepo;

    public TicketServiceImpl(TicketRepo ticketRepo) {
        this.ticketRepo = ticketRepo;
    }

    @Override
    public Ticket createTicket(Ticket ticket) {
        ticket.setId(null);
        return ticketRepo.save(ticket);
    }

    @Override
    public Ticket updateTicket(String email, Ticket updatedTicket) {

        Ticket ticket = ticketRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

        ticket.setPriority(updatedTicket.getPriority());
        ticket.setSummary(updatedTicket.getSummary());
        ticket.setStatus(updatedTicket.getStatus());
        ticket.setEmail(updatedTicket.getEmail());

        return ticketRepo.save(ticket);
    }

    @Override
    @Transactional(readOnly = true)
    public Ticket getTicketById(Long id) {
        return ticketRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
    }

    @Override
    public Ticket getTicketByEmail(String email) {
        return ticketRepo.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("Ticket not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ticket> getAllTickets() {
        return ticketRepo.findAll();
    }

    @Override
    public void deleteTicket(String user) {
        ticketRepo.deleteByEmail(user);
    }
}