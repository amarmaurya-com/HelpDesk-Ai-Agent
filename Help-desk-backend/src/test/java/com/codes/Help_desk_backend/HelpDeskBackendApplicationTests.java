package com.codes.Help_desk_backend;

import com.codes.Help_desk_backend.entity.Priority;
import com.codes.Help_desk_backend.entity.Status;
import com.codes.Help_desk_backend.entity.Ticket;
import com.codes.Help_desk_backend.repository.TicketRepo;
import com.codes.Help_desk_backend.service.TicketServiceImpl;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class HelpDeskBackendApplicationTests {

    @Test
    void createTicketDefaultsStatusToOpen() {
        TicketRepo ticketRepo = ticketRepoWithExistingTicket(null);
        TicketServiceImpl ticketService = new TicketServiceImpl(ticketRepo);
        Ticket ticket = Ticket.builder()
                .id(99L)
                .email("user@example.com")
                .summary("Printer issue")
                .priority(Priority.MEDIUM)
                .build();

        Ticket savedTicket = ticketService.createTicket(ticket);

        assertThat(savedTicket.getId()).isNull();
        assertThat(savedTicket.getStatus()).isEqualTo(Status.OPEN);
    }

    @Test
    void updateTicketAppliesOnlyProvidedFields() {
        Ticket existingTicket = Ticket.builder()
                .id(1L)
                .email("user@example.com")
                .summary("Old summary")
                .description("Old description")
                .category("Hardware")
                .priority(Priority.LOW)
                .status(Status.OPEN)
                .build();
        Ticket update = Ticket.builder()
                .description("Updated description")
                .priority(Priority.HIGH)
                .build();

        TicketRepo ticketRepo = ticketRepoWithExistingTicket(existingTicket);
        TicketServiceImpl ticketService = new TicketServiceImpl(ticketRepo);

        Ticket savedTicket = ticketService.updateTicket(123L, update);

        assertThat(savedTicket.getSummary()).isEqualTo("Old summary");
        assertThat(savedTicket.getDescription()).isEqualTo("Updated description");
        assertThat(savedTicket.getCategory()).isEqualTo("Hardware");
        assertThat(savedTicket.getPriority()).isEqualTo(Priority.HIGH);
        assertThat(savedTicket.getStatus()).isEqualTo(Status.OPEN);
    }

    private TicketRepo ticketRepoWithExistingTicket(Ticket existingTicket) {
        return (TicketRepo) Proxy.newProxyInstance(
                TicketRepo.class.getClassLoader(),
                new Class<?>[]{TicketRepo.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "findById" -> Optional.ofNullable(existingTicket);
                    case "findAllByEmail" -> existingTicket == null ? List.of() : List.of(existingTicket);
                    case "save" -> args[0];
                    default -> throw new UnsupportedOperationException(method.getName());
                });
    }
}
