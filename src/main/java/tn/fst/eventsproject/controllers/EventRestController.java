package tn.fst.eventsproject.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import tn.fst.eventsproject.dto.EventDTO;
import tn.fst.eventsproject.dto.LogisticsDTO;
import tn.fst.eventsproject.dto.ParticipantDTO;
import tn.fst.eventsproject.entities.Event;
import tn.fst.eventsproject.entities.Logistics;
import tn.fst.eventsproject.entities.Participant;
import tn.fst.eventsproject.mappers.EventMapper;
import tn.fst.eventsproject.services.IEventServices;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("event")
@RestController
public class EventRestController {
    private final IEventServices eventServices;
    private final EventMapper eventMapper;

    @PostMapping("/addPart")
    public ParticipantDTO addParticipant(@RequestBody ParticipantDTO participantDTO){
        Participant participant = eventMapper.toEntity(participantDTO);
        Participant savedParticipant = eventServices.addParticipant(participant);
        return eventMapper.toDTO(savedParticipant);
    }
    
    @PostMapping("/addEvent/{id}")
    public EventDTO addEventPart(@RequestBody EventDTO eventDTO, @PathVariable("id") int idPart){
        Event event = eventMapper.toEntity(eventDTO);
        Event savedEvent = eventServices.addAffectEvenParticipant(event, idPart);
        return eventMapper.toDTO(savedEvent);
    }
    
    @PostMapping("/addEvent")
    public EventDTO addEvent(@RequestBody EventDTO eventDTO){
        Event event = eventMapper.toEntity(eventDTO);
        Event savedEvent = eventServices.addAffectEvenParticipant(event);
        return eventMapper.toDTO(savedEvent);
    }
    
    @PutMapping("/addAffectLog/{description}")
    public LogisticsDTO addAffectLog(@RequestBody LogisticsDTO logisticsDTO, @PathVariable("description") String descriptionEvent){
        Logistics logistics = eventMapper.toEntity(logisticsDTO);
        Logistics savedLogistics = eventServices.addAffectLog(logistics, descriptionEvent);
        return eventMapper.toDTO(savedLogistics);
    }
    
    @GetMapping("/getLogs/{d1}/{d2}")
    public List<LogisticsDTO> getLogistiquesDates (
            @PathVariable("d1") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date_debut, 
            @PathVariable("d2") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date_fin){
        List<Logistics> logisticsList = eventServices.getLogisticsDates(date_debut, date_fin);
        if(logisticsList == null || logisticsList.isEmpty()){
            return new ArrayList<>();
        }
        return logisticsList.stream()
                .map(eventMapper::toDTO)
                .collect(Collectors.toList());
    }
}
