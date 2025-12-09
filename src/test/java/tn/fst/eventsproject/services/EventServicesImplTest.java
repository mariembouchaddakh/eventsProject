package tn.fst.eventsproject.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.fst.eventsproject.entities.Event;
import tn.fst.eventsproject.entities.Logistics;
import tn.fst.eventsproject.entities.Participant;
import tn.fst.eventsproject.entities.Tache;
import tn.fst.eventsproject.repositories.EventRepository;
import tn.fst.eventsproject.repositories.LogisticsRepository;
import tn.fst.eventsproject.repositories.ParticipantRepository;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServicesImplTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private LogisticsRepository logisticsRepository;

    @InjectMocks
    private EventServicesImpl eventServices;

    private Participant participant;
    private Event event;
    private Logistics logistics;

    @BeforeEach
    void setUp() {
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
    void testAddParticipant() {
        // Given
        when(participantRepository.save(any(Participant.class))).thenReturn(participant);

        // When
        Participant result = eventServices.addParticipant(participant);

        // Then
        assertNotNull(result);
        assertEquals(participant.getIdPart(), result.getIdPart());
        assertEquals(participant.getNom(), result.getNom());
        verify(participantRepository, times(1)).save(participant);
    }

    @Test
    void testAddAffectEvenParticipant_WithIdParticipant_WhenParticipantHasNoEvents() {
        // Given
        when(participantRepository.findById(anyInt())).thenReturn(Optional.of(participant));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        // When
        Event result = eventServices.addAffectEvenParticipant(event, 1);

        // Then
        assertNotNull(result);
        assertTrue(participant.getEvents().contains(event));
        verify(participantRepository, times(1)).findById(1);
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    void testAddAffectEvenParticipant_WithIdParticipant_WhenParticipantHasEvents() {
        // Given
        Set<Event> existingEvents = new HashSet<>();
        existingEvents.add(new Event());
        participant.setEvents(existingEvents);
        when(participantRepository.findById(anyInt())).thenReturn(Optional.of(participant));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        // When
        Event result = eventServices.addAffectEvenParticipant(event, 1);

        // Then
        assertNotNull(result);
        assertTrue(participant.getEvents().contains(event));
        verify(participantRepository, times(1)).findById(1);
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    void testAddAffectEvenParticipant_WithIdParticipant_WhenParticipantNotFound() {
        // Given
        when(participantRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            eventServices.addAffectEvenParticipant(event, 999);
        });

        verify(participantRepository, times(1)).findById(999);
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void testAddAffectEvenParticipant_WithEvent_WhenParticipantsHaveNoEvents() {
        // Given
        Set<Participant> participants = new HashSet<>();
        participants.add(participant);
        event.setParticipants(participants);

        when(participantRepository.findById(anyInt())).thenReturn(Optional.of(participant));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        // When
        Event result = eventServices.addAffectEvenParticipant(event);

        // Then
        assertNotNull(result);
        assertTrue(participant.getEvents().contains(event));
        verify(participantRepository, times(1)).findById(participant.getIdPart());
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    void testAddAffectEvenParticipant_WithEvent_WhenParticipantsHaveEvents() {
        // Given
        Set<Participant> participants = new HashSet<>();
        participants.add(participant);
        event.setParticipants(participants);

        Set<Event> existingEvents = new HashSet<>();
        existingEvents.add(new Event());
        participant.setEvents(existingEvents);

        when(participantRepository.findById(anyInt())).thenReturn(Optional.of(participant));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        // When
        Event result = eventServices.addAffectEvenParticipant(event);

        // Then
        assertNotNull(result);
        assertTrue(participant.getEvents().contains(event));
        verify(participantRepository, times(1)).findById(participant.getIdPart());
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    void testAddAffectLog_WhenEventHasNoLogistics() {
        // Given
        Event eventWithoutLogistics = new Event();
        eventWithoutLogistics.setIdEvent(1);
        eventWithoutLogistics.setDescription("Concert");
        eventWithoutLogistics.setLogistics(null); // Explicitly set to null
        
        when(eventRepository.findByDescription(anyString())).thenReturn(eventWithoutLogistics);
        when(logisticsRepository.save(any(Logistics.class))).thenReturn(logistics);
        when(eventRepository.save(any(Event.class))).thenReturn(eventWithoutLogistics);

        // When
        Logistics result = eventServices.addAffectLog(logistics, "Concert");

        // Then
        assertNotNull(result);
        assertNotNull(eventWithoutLogistics.getLogistics());
        assertTrue(eventWithoutLogistics.getLogistics().contains(logistics));
        verify(eventRepository, times(1)).findByDescription("Concert");
        verify(eventRepository, times(1)).save(any(Event.class));
        verify(logisticsRepository, times(1)).save(logistics);
    }

    @Test
    void testAddAffectLog_WhenEventHasLogistics() {
        // Given
        Set<Logistics> existingLogistics = new HashSet<>();
        existingLogistics.add(new Logistics());
        event.setLogistics(existingLogistics);

        when(eventRepository.findByDescription(anyString())).thenReturn(event);
        when(logisticsRepository.save(any(Logistics.class))).thenReturn(logistics);

        // When
        Logistics result = eventServices.addAffectLog(logistics, "Concert");

        // Then
        assertNotNull(result);
        assertTrue(event.getLogistics().contains(logistics));
        verify(eventRepository, times(1)).findByDescription("Concert");
        verify(logisticsRepository, times(1)).save(logistics);
    }

    @Test
    void testGetLogisticsDates_WhenEventsHaveReservedLogistics() {
        // Given
        Set<Logistics> logisticsSet = new HashSet<>();
        logisticsSet.add(logistics);
        event.setLogistics(logisticsSet);

        List<Event> events = new ArrayList<>();
        events.add(event);

        LocalDate dateDebut = LocalDate.of(2024, 1, 1);
        LocalDate dateFin = LocalDate.of(2024, 1, 31);

        when(eventRepository.findByDateDebutBetween(dateDebut, dateFin)).thenReturn(events);

        // When
        List<Logistics> result = eventServices.getLogisticsDates(dateDebut, dateFin);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(logistics));
        verify(eventRepository, times(1)).findByDateDebutBetween(dateDebut, dateFin);
    }

    @Test
    void testGetLogisticsDates_WhenEventsHaveNoLogistics() {
        // Given
        event.setLogistics(new HashSet<>());
        List<Event> events = new ArrayList<>();
        events.add(event);

        LocalDate dateDebut = LocalDate.of(2024, 1, 1);
        LocalDate dateFin = LocalDate.of(2024, 1, 31);

        when(eventRepository.findByDateDebutBetween(dateDebut, dateFin)).thenReturn(events);

        // When
        List<Logistics> result = eventServices.getLogisticsDates(dateDebut, dateFin);

        // Then
        assertNull(result);
        verify(eventRepository, times(1)).findByDateDebutBetween(dateDebut, dateFin);
    }

    @Test
    void testGetLogisticsDates_WhenLogisticsNotReserved() {
        // Given
        logistics.setReserve(false);
        Set<Logistics> logisticsSet = new HashSet<>();
        logisticsSet.add(logistics);
        event.setLogistics(logisticsSet);

        List<Event> events = new ArrayList<>();
        events.add(event);

        LocalDate dateDebut = LocalDate.of(2024, 1, 1);
        LocalDate dateFin = LocalDate.of(2024, 1, 31);

        when(eventRepository.findByDateDebutBetween(dateDebut, dateFin)).thenReturn(events);

        // When
        List<Logistics> result = eventServices.getLogisticsDates(dateDebut, dateFin);

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(eventRepository, times(1)).findByDateDebutBetween(dateDebut, dateFin);
    }

    @Test
    void testCalculCout() {
        // Given
        Set<Logistics> logisticsSet = new HashSet<>();
        logisticsSet.add(logistics);
        event.setLogistics(logisticsSet);

        List<Event> events = new ArrayList<>();
        events.add(event);

        when(eventRepository.findByParticipants_NomAndParticipants_PrenomAndParticipants_Tache(
                "Tounsi", "Ahmed", Tache.ORGANISATEUR)).thenReturn(events);
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        // When
        eventServices.calculCout();

        // Then
        assertEquals(200f, event.getCout()); // 100 * 2 = 200
        verify(eventRepository, times(1)).findByParticipants_NomAndParticipants_PrenomAndParticipants_Tache(
                "Tounsi", "Ahmed", Tache.ORGANISATEUR);
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    void testCalculCout_WithMultipleLogistics() {
        // Given
        Logistics logistics2 = new Logistics();
        logistics2.setReserve(true);
        logistics2.setPrixUnit(50f);
        logistics2.setQuantite(3);

        Set<Logistics> logisticsSet = new HashSet<>();
        logisticsSet.add(logistics);
        logisticsSet.add(logistics2);
        event.setLogistics(logisticsSet);

        List<Event> events = new ArrayList<>();
        events.add(event);

        when(eventRepository.findByParticipants_NomAndParticipants_PrenomAndParticipants_Tache(
                "Tounsi", "Ahmed", Tache.ORGANISATEUR)).thenReturn(events);
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        // When
        eventServices.calculCout();

        // Then
        assertEquals(350f, event.getCout()); // (100 * 2) + (50 * 3) = 350
        verify(eventRepository, times(1)).findByParticipants_NomAndParticipants_PrenomAndParticipants_Tache(
                "Tounsi", "Ahmed", Tache.ORGANISATEUR);
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    void testCalculCout_WithNonReservedLogistics() {
        // Given
        logistics.setReserve(false);
        Set<Logistics> logisticsSet = new HashSet<>();
        logisticsSet.add(logistics);
        event.setLogistics(logisticsSet);

        List<Event> events = new ArrayList<>();
        events.add(event);

        when(eventRepository.findByParticipants_NomAndParticipants_PrenomAndParticipants_Tache(
                "Tounsi", "Ahmed", Tache.ORGANISATEUR)).thenReturn(events);
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        // When
        eventServices.calculCout();

        // Then
        assertEquals(0f, event.getCout());
        verify(eventRepository, times(1)).findByParticipants_NomAndParticipants_PrenomAndParticipants_Tache(
                "Tounsi", "Ahmed", Tache.ORGANISATEUR);
        verify(eventRepository, times(1)).save(event);
    }
}

