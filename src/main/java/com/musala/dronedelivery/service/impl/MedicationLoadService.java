package com.musala.dronedelivery.service.impl;

import com.musala.dronedelivery.dto.medicationLoad.LoadCreationDTO;
import com.musala.dronedelivery.dto.medicationLoad.MedicationLoadDTO;
import com.musala.dronedelivery.entity.Drone;
import com.musala.dronedelivery.generic.Load;
import com.musala.dronedelivery.entity.Medication;
import com.musala.dronedelivery.enums.DroneState;
import com.musala.dronedelivery.exception.AppException;
import com.musala.dronedelivery.repository.LoadRepository;
import com.musala.dronedelivery.service.LoadService;
import com.musala.dronedelivery.service.MedicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.musala.dronedelivery.mapper.MedicationLoadMapper.MEDICATION_LOAD_MAPPER;


@Slf4j
@Service
@RequiredArgsConstructor
public class MedicationLoadService implements LoadService<Medication> {

    private final LoadRepository loadRepository;
    private final MedicationService medicationService;

    @Transactional
    @Override
    public MedicationLoadDTO save(Drone drone, LoadCreationDTO medicationLoadDTO) throws AppException {

        validateMedicationLoadDTO(medicationLoadDTO);

        drone.setState(DroneState.LOADING);

        List<Medication> medications = medicationService.getMedicationsFromMedicationLoadDTO(medicationLoadDTO);

        BigDecimal totalWeight = calculateTotalWeight(medications);
        if (totalWeight.compareTo(drone.getWeightLimit()) > 0) {
            drone.setState(DroneState.IDLE);
            log.error("The weight of the load exceeds the weight limit of the drone. Total weight: {}", totalWeight);
            throw new AppException(HttpStatus.UNPROCESSABLE_ENTITY, "The weight of the load exceeds the max allowed");
        }

        Load<Medication> medicationLoad = new Load<>(drone, medications);

        drone.setState(DroneState.LOADED);

        log.info("Loaded '{}' with '{}' ", drone.getSerialNumber(), medicationLoadDTO);
        medicationLoad = loadRepository.save(medicationLoad);


        return MEDICATION_LOAD_MAPPER.toLoadCheckDTO(medicationLoad);
    }


    @Override
    public List<LoadCreationDTO> findAll() {
        return loadRepository.findAll()
                .stream()
                .map(MEDICATION_LOAD_MAPPER::entityToDTO)
                .toList();
    }

    @Override
    public MedicationLoadDTO findByDroneSerialNumber(String serialNumber) {

        Optional<Load> load = loadRepository.findByDroneSerialNumber(serialNumber);

        return load.map(MEDICATION_LOAD_MAPPER::toLoadCheckDTO)
                .orElseGet(() -> MedicationLoadDTO.builder()
                .carrier(serialNumber)
                .medicationCodes(Collections.emptyList())
                .build());

    }

    @Override
    public BigDecimal calculateTotalWeight(List<Medication> medications) {
            return medications.stream()
                    .map(Medication::getWeight)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validateMedicationLoadDTO(LoadCreationDTO medicationLoadDTO) throws AppException {
        Set<String> medicationCodes = medicationLoadDTO.getItemCodes();
        List<String> existingMedicationCodes = medicationService.findExistingMedicationCodes(medicationCodes);

        if (existingMedicationCodes.isEmpty()) {
            log.error("None of the medications in the list were found in the database");
            throw new AppException(HttpStatus.UNPROCESSABLE_ENTITY, "None of the medications in the list were found in the database");
        }

        if (medicationCodes.size() != existingMedicationCodes.size()) {
            String nonExistentCodes = medicationCodes.stream()
                    .filter(code -> !existingMedicationCodes.contains(code))
                    .collect(Collectors.joining(", "));
            log.error("The following codes do not match with any medication in the catalog: '{}'", nonExistentCodes);
            throw new AppException(HttpStatus.UNPROCESSABLE_ENTITY, "The following medications do not exist in the medication catalog: " + nonExistentCodes);
        }
    }


}
