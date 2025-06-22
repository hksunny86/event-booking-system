package com.example.ticketservice;

import com.example.ticketservice.controller.TicketController;
import com.example.ticketservice.dto.BookTicketRequest;
import com.example.ticketservice.dto.EventResponse;
import com.example.ticketservice.entity.Ticket;
import com.example.ticketservice.repository.TicketRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketRepository ticketRepository;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    void shouldBookTicketWhenEventAvailable() throws Exception {
        BookTicketRequest request = new BookTicketRequest(1L, "Hamza", "VIP", 2, BigDecimal.valueOf(1000));
        EventResponse event = new EventResponse(1L, "Show", LocalDate.now(), "Lahore", 10, BigDecimal.valueOf(500));

        when(restTemplate.getForObject(anyString(), eq(EventResponse.class))).thenReturn(event);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(new Ticket());

        mockMvc.perform(post("/tickets/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCancelTicket() throws Exception {
        Ticket ticket = Ticket.builder()
                .id(1L)
                .eventName("Expo")
                .status("BOOKED")
                .quantity(1)
                .paymentAmount(BigDecimal.valueOf(100))
                .build();

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any())).thenReturn(ticket);

        mockMvc.perform(post("/tickets/cancel/1"))
                .andExpect(status().isOk());
    }
}
