package com.musala.dronedelivery.service;

import com.musala.dronedelivery.dto.medicationLoad.LoadCreationDTO;
import com.musala.dronedelivery.dto.medicationLoad.MedicationLoadDTO;
import com.musala.dronedelivery.entity.Drone;
import com.musala.dronedelivery.exception.AppException;

import java.math.BigDecimal;
import java.util.List;

public interface LoadService<E> {

    MedicationLoadDTO save(Drone drone, LoadCreationDTO medicationLoadDTO) throws AppException;

    List<LoadCreationDTO> findAll();

    MedicationLoadDTO findByDroneSerialNumber(String serialNumber);

    BigDecimal calculateTotalWeight(List<E> load);
}
