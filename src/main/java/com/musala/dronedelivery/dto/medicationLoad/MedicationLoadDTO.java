package com.musala.dronedelivery.dto.medicationLoad;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class MedicationLoadDTO {

    private String carrier;
    private List<String> medicationCodes;

}
