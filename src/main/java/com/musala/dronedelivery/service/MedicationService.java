package com.musala.dronedelivery.service;

import com.musala.dronedelivery.dto.medication.MedicationCreationDTO;
import com.musala.dronedelivery.dto.medication.MedicationDTO;
import com.musala.dronedelivery.dto.medicationLoad.LoadCreationDTO;
import com.musala.dronedelivery.entity.Medication;
import com.musala.dronedelivery.exception.AppException;

import java.util.List;
import java.util.Set;

public interface MedicationService {

    MedicationDTO save(MedicationCreationDTO medicationDTO) throws AppException;

    List<MedicationCreationDTO> findAll();

    List<String> findExistingMedicationCodes(Set<String> codes);

    List<Medication> getMedicationsFromMedicationLoadDTO(LoadCreationDTO medicationLoadDTO);
}
