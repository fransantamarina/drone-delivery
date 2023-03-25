package com.musala.dronedelivery.service.impl;


import com.musala.dronedelivery.dto.medication.MedicationCreationDTO;
import com.musala.dronedelivery.dto.medication.MedicationDTO;
import com.musala.dronedelivery.dto.medicationLoad.LoadCreationDTO;
import com.musala.dronedelivery.entity.Medication;
import com.musala.dronedelivery.exception.AppException;
import com.musala.dronedelivery.repository.MedicationRepository;
import com.musala.dronedelivery.service.MedicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static com.musala.dronedelivery.mapper.MedicationMapper.MEDICATION_MAPPER;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicationServiceImpl implements MedicationService {

    private final MedicationRepository medicationRepository;

    @Override
    public MedicationDTO save(MedicationCreationDTO medicationCreationDTO) throws AppException {
        if (medicationRepository.existsByName(medicationCreationDTO.getName())) {
            log.error("There is already a medication with name: '{}'", medicationCreationDTO.getName());
            throw new AppException(HttpStatus.CONFLICT, "Medication with name: " + medicationCreationDTO.getName() + " already exists");
        }

        if (medicationRepository.existsByCode(medicationCreationDTO.getCode())) {
            log.error("There is already a medication with code: '{}'", medicationCreationDTO.getCode());
            throw new AppException(HttpStatus.CONFLICT, "Medication with code: " + medicationCreationDTO.getCode() + " already exists");
        }

        var medication = MEDICATION_MAPPER.medicationCreationDtoToEntity(medicationCreationDTO);
        medication = medicationRepository.save(medication);

        log.info("Created medication=  Code: '{}' - Name: '{}' ", medication.getCode(), medication.getName());
        return MEDICATION_MAPPER.entityToDTO(medication);
    }

    @Override
    public List<MedicationCreationDTO> findAll() {
        return null;
    }

    @Override
    public List<String> findExistingMedicationCodes(Set<String> codes) {
        return medicationRepository.findExistingCodes(codes);
    }


    @Override
    public List<Medication> getMedicationsFromMedicationLoadDTO(LoadCreationDTO medicationLoadDTO) {

        Set<String> medicationCodes = medicationLoadDTO.getItemCodes();

        return medicationRepository.findAllByCodeIn(medicationCodes);
    }

}
