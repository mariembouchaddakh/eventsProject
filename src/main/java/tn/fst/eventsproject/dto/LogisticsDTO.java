package tn.fst.eventsproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LogisticsDTO implements Serializable {
    Integer idLog;
    String description;
    Boolean reserve;
    Float prixUnit;
    Integer quantite;
}

