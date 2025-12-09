package tn.fst.eventsproject.mappers;

import org.springframework.stereotype.Component;
import tn.fst.eventsproject.dto.EventDTO;
import tn.fst.eventsproject.dto.LogisticsDTO;
import tn.fst.eventsproject.dto.ParticipantDTO;
import tn.fst.eventsproject.entities.Event;
import tn.fst.eventsproject.entities.Logistics;
import tn.fst.eventsproject.entities.Participant;

@Component
public class EventMapper {

    public EventDTO toDTO(Event event) {
        if (event == null) {
            return null;
        }
        EventDTO dto = new EventDTO();
        dto.setIdEvent(event.getIdEvent());
        dto.setDescription(event.getDescription());
        dto.setDateDebut(event.getDateDebut());
        dto.setDateFin(event.getDateFin());
        dto.setCout(event.getCout());
        return dto;
    }

    public Event toEntity(EventDTO dto) {
        if (dto == null) {
            return null;
        }
        Event event = new Event();
        event.setIdEvent(dto.getIdEvent());
        event.setDescription(dto.getDescription());
        event.setDateDebut(dto.getDateDebut());
        event.setDateFin(dto.getDateFin());
        event.setCout(dto.getCout() != null ? dto.getCout() : 0f);
        return event;
    }

    public ParticipantDTO toDTO(Participant participant) {
        if (participant == null) {
            return null;
        }
        ParticipantDTO dto = new ParticipantDTO();
        dto.setIdPart(participant.getIdPart());
        dto.setNom(participant.getNom());
        dto.setPrenom(participant.getPrenom());
        dto.setTache(participant.getTache());
        return dto;
    }

    public Participant toEntity(ParticipantDTO dto) {
        if (dto == null) {
            return null;
        }
        Participant participant = new Participant();
        participant.setIdPart(dto.getIdPart());
        participant.setNom(dto.getNom());
        participant.setPrenom(dto.getPrenom());
        participant.setTache(dto.getTache());
        return participant;
    }

    public LogisticsDTO toDTO(Logistics logistics) {
        if (logistics == null) {
            return null;
        }
        LogisticsDTO dto = new LogisticsDTO();
        dto.setIdLog(logistics.getIdLog());
        dto.setDescription(logistics.getDescription());
        dto.setReserve(logistics.isReserve());
        dto.setPrixUnit(logistics.getPrixUnit());
        dto.setQuantite(logistics.getQuantite());
        return dto;
    }

    public Logistics toEntity(LogisticsDTO dto) {
        if (dto == null) {
            return null;
        }
        Logistics logistics = new Logistics();
        logistics.setIdLog(dto.getIdLog());
        logistics.setDescription(dto.getDescription());
        logistics.setReserve(dto.getReserve() != null ? dto.getReserve() : false);
        logistics.setPrixUnit(dto.getPrixUnit() != null ? dto.getPrixUnit() : 0f);
        logistics.setQuantite(dto.getQuantite() != null ? dto.getQuantite() : 0);
        return logistics;
    }
}

