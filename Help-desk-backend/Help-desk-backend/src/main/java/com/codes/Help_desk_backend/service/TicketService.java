package com.codes.Help_desk_backend.service;

import com.codes.Help_desk_backend.entity.Ticket;

import java.util.List;

public interface TicketService {

    Ticket createTicket(Ticket ticket);

    Ticket updateTicket(String email, Ticket ticket);

    Ticket getTicketById(Long id);

    Ticket getTicketByEmail(String email);

    List<Ticket> getAllTickets();

    void deleteTicket(String user);
}