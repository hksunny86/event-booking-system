package com.example.eventservice;

import com.example.eventservice.controller.EventController;
import com.example.eventservice.entity.Event;
import com.example.eventservice.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
class EventServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Test
    void shouldCreateEvent() throws Exception {
        Event event = new Event(1L, "Concert", LocalDate.now(), "Lahore", 100, BigDecimal.valueOf(500));
        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(event)))
                .andExpect(status().isOk());
        verify(eventService).createEvent(any(Event.class));
    }

    @Test
    void shouldGetAllEvents() throws Exception {
        when(eventService.findAll()).thenReturn(List.of(new Event()));
        mockMvc.perform(get("/events"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnEventById() throws Exception {
        Event event = new Event(1L, "Fair", LocalDate.now(), "Karachi", 50, BigDecimal.valueOf(300));
        when(eventService.findById(1L)).thenReturn(Optional.of(event));
        mockMvc.perform(get("/events/1"))
                .andExpect(status().isOk());
    }
}
