package com.codes.Help_desk_backend.Exceptions;

public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException(String ticketNotFound) {
        super(ticketNotFound);
    }
}
