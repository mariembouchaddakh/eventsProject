package tn.fst.eventsproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import tn.fst.eventsproject.entities.Tache;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipantDTO implements Serializable {
    Integer idPart;
    String nom;
    String prenom;
    Tache tache;
}

