package com.musala.dronedelivery.service.impl;

import com.musala.dronedelivery.dto.drone.DroneBatteryDTO;
import com.musala.dronedelivery.dto.drone.DroneCreationDTO;
import com.musala.dronedelivery.dto.medicationLoad.LoadCreationDTO;
import com.musala.dronedelivery.dto.medicationLoad.MedicationLoadDTO;
import com.musala.dronedelivery.entity.Drone;
import com.musala.dronedelivery.enums.DroneState;
import com.musala.dronedelivery.exception.AppException;
import com.musala.dronedelivery.repository.DroneRepository;
import com.musala.dronedelivery.service.DroneService;
import com.musala.dronedelivery.service.LoadService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.musala.dronedelivery.mapper.DroneMapper.DRONE_MAPPER;
import static com.musala.dronedelivery.util.DroneConstants.DEFAULT_WEIGHT_LIMIT;
import static com.musala.dronedelivery.util.DroneConstants.MINIMUM_BATTERY_CAPACITY_FOR_LOAD;

@Slf4j
@AllArgsConstructor
@Service
public class DroneServiceImpl implements DroneService {

    private final DroneRepository droneRepository;

    private final LoadService loadService;

    @Override
    public DroneCreationDTO register(DroneCreationDTO droneCreationDTO) throws AppException {

        if (droneRepository.existsBySerialNumber(droneCreationDTO.getSerialNumber())) {
            var errorMessage = String.format("Drone already exists: %s", droneCreationDTO.getSerialNumber());
            log.error(errorMessage);
            throw new AppException(HttpStatus.CONFLICT, errorMessage);
        }

        droneCreationDTO.setModel(droneCreationDTO.getModel().toUpperCase());

        var drone = DRONE_MAPPER.dtoToEntity(droneCreationDTO);

        drone.setState(DroneState.IDLE);
        drone.setBatteryCapacity(100);

        if (null == droneCreationDTO.getWeightLimit()) {
            drone.setWeightLimit(DEFAULT_WEIGHT_LIMIT);
        }

        drone = droneRepository.save(drone);
        log.info("Registered new drone - '{}'", drone);
        return DRONE_MAPPER.entityToDTO(drone);
    }

    @Override
    public List<DroneCreationDTO> findAll() {
        return droneRepository.findAll().stream()
                .map(DRONE_MAPPER::entityToDTO)
                .toList();
    }

    @Override
    public List<DroneCreationDTO> findAvailable() {
        return droneRepository.findAvailable(DroneState.IDLE, MINIMUM_BATTERY_CAPACITY_FOR_LOAD)
                .stream()
                .map(DRONE_MAPPER::entityToDTO)
                .toList();
    }

    @Override
    public DroneBatteryDTO getBateryLevel(String serialNumber) throws AppException {

        validateDroneExistence(serialNumber);

        Drone drone = droneRepository.findBySerialNumber(serialNumber);

        return DRONE_MAPPER.droneToBatteryDTO(drone);
    }

    @Transactional
    @Override
    public MedicationLoadDTO load(String serialNumber, LoadCreationDTO medicationLoadDTO) throws AppException {

        validateDroneExistence(serialNumber);

        Drone drone = validateDroneAvailability(serialNumber);

        return loadService.save(drone, medicationLoadDTO);
    }

    @Override
    public MedicationLoadDTO checkLoad(String serialNumber) throws AppException {
        validateDroneExistence(serialNumber);
        return loadService.findByDroneSerialNumber(serialNumber);
    }

    private boolean validateDroneExistence(String serialNumber) throws AppException {

        if (!droneRepository.existsBySerialNumber(serialNumber)) {
            var errorMessage = String.format("Drone does not exist: %s", serialNumber);
            log.error(errorMessage);
            throw new AppException(HttpStatus.NOT_FOUND, errorMessage);
        }
        return true;
    }

    private Drone validateDroneAvailability(String serialNumber) throws AppException {
        return droneRepository.findBySerialNumberAndStateAndBatteryCapacityGreaterThanEqual(serialNumber, DroneState.IDLE, MINIMUM_BATTERY_CAPACITY_FOR_LOAD)
                .orElseThrow(() -> {
                    log.error("Drone with serial number: '{}' is not available", serialNumber);
                    return new AppException(HttpStatus.CONFLICT, "Drone is not available: " + serialNumber);
                });
    }

    @Override
    public List<Drone> getAllDrones() {
        return droneRepository.findAll();
    }
}
