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
import tn.fst.eventsproject.dto.EventDTO;
import tn.fst.eventsproject.dto.LogisticsDTO;
import tn.fst.eventsproject.dto.ParticipantDTO;
import tn.fst.eventsproject.entities.Event;
import tn.fst.eventsproject.entities.Logistics;
import tn.fst.eventsproject.entities.Participant;
import tn.fst.eventsproject.entities.Tache;
import tn.fst.eventsproject.mappers.EventMapper;
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

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventRestController eventRestController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Participant participant;
    private Event event;
    private Logistics logistics;
    private ParticipantDTO participantDTO;
    private EventDTO eventDTO;
    private LogisticsDTO logisticsDTO;

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

        // DTOs
        participantDTO = new ParticipantDTO();
        participantDTO.setIdPart(1);
        participantDTO.setNom("Tounsi");
        participantDTO.setPrenom("Ahmed");
        participantDTO.setTache(Tache.ORGANISATEUR);

        eventDTO = new EventDTO();
        eventDTO.setIdEvent(1);
        eventDTO.setDescription("Concert");
        eventDTO.setDateDebut(LocalDate.of(2024, 1, 15));
        eventDTO.setDateFin(LocalDate.of(2024, 1, 20));
        eventDTO.setCout(0f);

        logisticsDTO = new LogisticsDTO();
        logisticsDTO.setIdLog(1);
        logisticsDTO.setDescription("Microphone");
        logisticsDTO.setReserve(true);
        logisticsDTO.setPrixUnit(100f);
        logisticsDTO.setQuantite(2);
    }

    @Test
    void testAddParticipant() throws Exception {
        // Given
        when(eventMapper.toEntity(any(ParticipantDTO.class))).thenReturn(participant);
        when(eventServices.addParticipant(any(Participant.class))).thenReturn(participant);
        when(eventMapper.toDTO(any(Participant.class))).thenReturn(participantDTO);

        // When & Then
        mockMvc.perform(post("/event/addPart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(participantDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPart").value(1))
                .andExpect(jsonPath("$.nom").value("Tounsi"))
                .andExpect(jsonPath("$.prenom").value("Ahmed"));

        verify(eventMapper, times(1)).toEntity(any(ParticipantDTO.class));
        verify(eventServices, times(1)).addParticipant(any(Participant.class));
        verify(eventMapper, times(1)).toDTO(any(Participant.class));
    }

    @Test
    void testAddEventPart() throws Exception {
        // Given
        when(eventMapper.toEntity(any(EventDTO.class))).thenReturn(event);
        when(eventServices.addAffectEvenParticipant(any(Event.class), anyInt())).thenReturn(event);
        when(eventMapper.toDTO(any(Event.class))).thenReturn(eventDTO);

        // When & Then
        mockMvc.perform(post("/event/addEvent/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEvent").value(1))
                .andExpect(jsonPath("$.description").value("Concert"));

        verify(eventMapper, times(1)).toEntity(any(EventDTO.class));
        verify(eventServices, times(1)).addAffectEvenParticipant(any(Event.class), eq(1));
        verify(eventMapper, times(1)).toDTO(any(Event.class));
    }

    @Test
    void testAddEvent() throws Exception {
        // Given
        when(eventMapper.toEntity(any(EventDTO.class))).thenReturn(event);
        when(eventServices.addAffectEvenParticipant(any(Event.class))).thenReturn(event);
        when(eventMapper.toDTO(any(Event.class))).thenReturn(eventDTO);

        // When & Then
        mockMvc.perform(post("/event/addEvent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEvent").value(1))
                .andExpect(jsonPath("$.description").value("Concert"));

        verify(eventMapper, times(1)).toEntity(any(EventDTO.class));
        verify(eventServices, times(1)).addAffectEvenParticipant(any(Event.class));
        verify(eventMapper, times(1)).toDTO(any(Event.class));
    }

    @Test
    void testAddAffectLog() throws Exception {
        // Given
        when(eventMapper.toEntity(any(LogisticsDTO.class))).thenReturn(logistics);
        when(eventServices.addAffectLog(any(Logistics.class), anyString())).thenReturn(logistics);
        when(eventMapper.toDTO(any(Logistics.class))).thenReturn(logisticsDTO);

        // When & Then
        mockMvc.perform(put("/event/addAffectLog/Concert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logisticsDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idLog").value(1))
                .andExpect(jsonPath("$.description").value("Microphone"))
                .andExpect(jsonPath("$.reserve").value(true));

        verify(eventMapper, times(1)).toEntity(any(LogisticsDTO.class));
        verify(eventServices, times(1)).addAffectLog(any(Logistics.class), eq("Concert"));
        verify(eventMapper, times(1)).toDTO(any(Logistics.class));
    }

    @Test
    void testGetLogistiquesDates() throws Exception {
        // Given
        List<Logistics> logisticsList = new ArrayList<>();
        logisticsList.add(logistics);
        List<LogisticsDTO> logisticsDTOList = new ArrayList<>();
        logisticsDTOList.add(logisticsDTO);

        LocalDate dateDebut = LocalDate.of(2024, 1, 1);
        LocalDate dateFin = LocalDate.of(2024, 1, 31);

        when(eventServices.getLogisticsDates(dateDebut, dateFin)).thenReturn(logisticsList);
        when(eventMapper.toDTO(any(Logistics.class))).thenReturn(logisticsDTO);

        // When & Then
        mockMvc.perform(get("/event/getLogs/2024-01-01/2024-01-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idLog").value(1))
                .andExpect(jsonPath("$[0].description").value("Microphone"))
                .andExpect(jsonPath("$[0].reserve").value(true));

        verify(eventServices, times(1)).getLogisticsDates(dateDebut, dateFin);
        verify(eventMapper, times(1)).toDTO(any(Logistics.class));
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

        when(eventServices.getLogisticsDates(dateDebut, dateFin)).thenReturn(new ArrayList<>());

        // When & Then
        mockMvc.perform(get("/event/getLogs/2024-01-01/2024-01-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(eventServices, times(1)).getLogisticsDates(dateDebut, dateFin);
    }
}

