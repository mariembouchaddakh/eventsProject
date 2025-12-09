package tn.fst.eventsproject.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.fst.eventsproject.entities.Event;
import tn.fst.eventsproject.entities.Logistics;
import tn.fst.eventsproject.entities.Participant;
import tn.fst.eventsproject.entities.Tache;
import tn.fst.eventsproject.services.IEventServices;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EventRestControllerTest {

    @Mock
    private IEventServices eventServices;

    @InjectMocks
    private EventRestController eventRestController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Participant participant;
    private Event event;
    private Logistics logistics;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(eventRestController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        participant = new Participant();
        participant.setIdPart(1);
        participant.setNom("Tounsi");
        participant.setPrenom("Ahmed");
        participant.setTache(Tache.ORGANISATEUR);
        participant.setEvents(new HashSet<>());

        event = new Event();
        event.setIdEvent(1);
        event.setDescription("Concert");
        event.setDateDebut(LocalDate.of(2024, 1, 15));
        event.setDateFin(LocalDate.of(2024, 1, 20));
        event.setCout(0f);
        event.setParticipants(new HashSet<>());
        event.setLogistics(new HashSet<>());

        logistics = new Logistics();
        logistics.setIdLog(1);
        logistics.setDescription("Microphone");
        logistics.setReserve(true);
        logistics.setPrixUnit(100f);
        logistics.setQuantite(2);
    }

    @Test
    void testAddParticipant() throws Exception {
        // Given
        when(eventServices.addParticipant(any(Participant.class))).thenReturn(participant);

        // When & Then
        mockMvc.perform(post("/event/addPart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(participant)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPart").value(1))
                .andExpect(jsonPath("$.nom").value("Tounsi"))
                .andExpect(jsonPath("$.prenom").value("Ahmed"));

        verify(eventServices, times(1)).addParticipant(any(Participant.class));
    }

    @Test
    void testAddEventPart() throws Exception {
        // Given
        when(eventServices.addAffectEvenParticipant(any(Event.class), anyInt())).thenReturn(event);

        // When & Then
        mockMvc.perform(post("/event/addEvent/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEvent").value(1))
                .andExpect(jsonPath("$.description").value("Concert"));

        verify(eventServices, times(1)).addAffectEvenParticipant(any(Event.class), eq(1));
    }

    @Test
    void testAddEvent() throws Exception {
        // Given
        when(eventServices.addAffectEvenParticipant(any(Event.class))).thenReturn(event);

        // When & Then
        mockMvc.perform(post("/event/addEvent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEvent").value(1))
                .andExpect(jsonPath("$.description").value("Concert"));

        verify(eventServices, times(1)).addAffectEvenParticipant(any(Event.class));
    }

    @Test
    void testAddAffectLog() throws Exception {
        // Given
        when(eventServices.addAffectLog(any(Logistics.class), anyString())).thenReturn(logistics);

        // When & Then
        mockMvc.perform(put("/event/addAffectLog/Concert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logistics)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idLog").value(1))
                .andExpect(jsonPath("$.description").value("Microphone"))
                .andExpect(jsonPath("$.reserve").value(true));

        verify(eventServices, times(1)).addAffectLog(any(Logistics.class), eq("Concert"));
    }

    @Test
    void testGetLogistiquesDates() throws Exception {
        // Given
        List<Logistics> logisticsList = new ArrayList<>();
        logisticsList.add(logistics);

        LocalDate dateDebut = LocalDate.of(2024, 1, 1);
        LocalDate dateFin = LocalDate.of(2024, 1, 31);

        when(eventServices.getLogisticsDates(dateDebut, dateFin)).thenReturn(logisticsList);

        // When & Then
        mockMvc.perform(get("/event/getLogs/2024-01-01/2024-01-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idLog").value(1))
                .andExpect(jsonPath("$[0].description").value("Microphone"))
                .andExpect(jsonPath("$[0].reserve").value(true));

        verify(eventServices, times(1)).getLogisticsDates(dateDebut, dateFin);
    }

    @Test
    void testGetLogistiquesDates_EmptyList() throws Exception {
        // Given
        List<Logistics> emptyList = new ArrayList<>();
        LocalDate dateDebut = LocalDate.of(2024, 1, 1);
        LocalDate dateFin = LocalDate.of(2024, 1, 31);

        when(eventServices.getLogisticsDates(dateDebut, dateFin)).thenReturn(emptyList);

        // When & Then
        mockMvc.perform(get("/event/getLogs/2024-01-01/2024-01-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(eventServices, times(1)).getLogisticsDates(dateDebut, dateFin);
    }

    @Test
    void testGetLogistiquesDates_NullResult() throws Exception {
        // Given
        LocalDate dateDebut = LocalDate.of(2024, 1, 1);
        LocalDate dateFin = LocalDate.of(2024, 1, 31);

        when(eventServices.getLogisticsDates(dateDebut, dateFin)).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/event/getLogs/2024-01-01/2024-01-31"))
                .andExpect(status().isOk());

        verify(eventServices, times(1)).getLogisticsDates(dateDebut, dateFin);
    }
}

