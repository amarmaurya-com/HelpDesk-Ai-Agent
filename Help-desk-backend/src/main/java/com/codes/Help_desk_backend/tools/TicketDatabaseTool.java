package com.codes.Help_desk_backend.tools;

import com.codes.Help_desk_backend.entity.Ticket;
import com.codes.Help_desk_backend.service.TicketService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class TicketDatabaseTool {
    private final TicketService ticketService;
    public TicketDatabaseTool(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Tool(description = "This tools helps to create new tickets in database")
    public Ticket createTicket(@ToolParam(description = "Ticket fields required to create ne tickets") Ticket ticket) {
        System.out.println(ticket);
        return ticketService.createTicket(ticket);
    }

    @Tool(description = "This tool help to get the ticket by email")
    public Ticket getMyTicket(@ToolParam(description = " email id whose ticket is required ") String emailid) {
        return ticketService.getTicketByEmail(emailid);
    }

    @Tool(description = "Update an existing ticket for a user.")
    public Ticket updateTicket(
            @ToolParam(description = "email id of the ticket owner.")
            String email,
            @ToolParam(description = "Updated ticket details including the ticket ID.")
            Ticket ticket) {

        return ticketService.updateTicket(email, ticket);
    }

    @Tool(description = "This tool is for to delete the Ticket form the Database")
    public void deleteTicket(@ToolParam(description = "Email id of the user whose ticket is need to delete") String email) {
        ticketService.deleteTicket(email);
    }

    @Tool(description = "this tool help tp get current system time")
    public String getCurrent(){
        return String.valueOf(System.currentTimeMillis());
    }
}