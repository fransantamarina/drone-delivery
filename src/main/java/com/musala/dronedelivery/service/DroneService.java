package com.musala.dronedelivery.service;

import com.musala.dronedelivery.dto.drone.DroneBatteryDTO;
import com.musala.dronedelivery.dto.drone.DroneCreationDTO;
import com.musala.dronedelivery.dto.medicationLoad.LoadCreationDTO;
import com.musala.dronedelivery.dto.medicationLoad.MedicationLoadDTO;
import com.musala.dronedelivery.entity.Drone;
import com.musala.dronedelivery.exception.AppException;

import java.util.List;

public interface DroneService {

    DroneCreationDTO register(DroneCreationDTO droneCreationDTO) throws AppException;

    List<DroneCreationDTO> findAll();

    List<DroneCreationDTO> findAvailable();

    DroneBatteryDTO getBateryLevel(String serialNumber) throws AppException;

    MedicationLoadDTO load(String droneSerialNumber, LoadCreationDTO medicationLoadDTO) throws AppException;

    MedicationLoadDTO checkLoad(String serialNumber) throws AppException;

    List<Drone> getAllDrones();
}
