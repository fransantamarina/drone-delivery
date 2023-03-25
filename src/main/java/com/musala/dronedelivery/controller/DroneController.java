package com.musala.dronedelivery.controller;

import com.musala.dronedelivery.dto.drone.DroneBatteryDTO;
import com.musala.dronedelivery.dto.drone.DroneCreationDTO;
import com.musala.dronedelivery.dto.medicationLoad.LoadCreationDTO;
import com.musala.dronedelivery.dto.medicationLoad.MedicationLoadDTO;
import com.musala.dronedelivery.exception.AppException;
import com.musala.dronedelivery.service.DroneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drones")
@RequiredArgsConstructor
public class DroneController {

    private final DroneService droneService;

    @PostMapping
    public ResponseEntity<DroneCreationDTO> createDrone(@RequestBody @Valid DroneCreationDTO droneCreationDTO) throws AppException {
        return ResponseEntity.status(HttpStatus.CREATED).body(droneService.register(droneCreationDTO));
    }

    @GetMapping
    public ResponseEntity<List<DroneCreationDTO>> getAllDrones() {
        return ResponseEntity.ok(droneService.findAll());
    }

    @GetMapping("/available")
    public ResponseEntity<List<DroneCreationDTO>> getAvailableDrones() {
        return ResponseEntity.ok(droneService.findAvailable());
    }

    @GetMapping("/{serialNumber}/battery")
    public ResponseEntity<DroneBatteryDTO> getBatteryLevel(@PathVariable String serialNumber) throws AppException {
        return ResponseEntity.ok(droneService.getBateryLevel(serialNumber));
    }

    @GetMapping("/{serialNumber}/medications")
    public ResponseEntity<MedicationLoadDTO> getDroneMedications(@PathVariable String serialNumber) throws AppException {
        return ResponseEntity.ok(droneService.checkLoad(serialNumber));
    }

    @PostMapping("/{serialNumber}/medications")
    public ResponseEntity<MedicationLoadDTO> loadDrone(@PathVariable String serialNumber, @RequestBody @Valid LoadCreationDTO medicationCodes) throws AppException {
        return ResponseEntity.ok(droneService.load(serialNumber, medicationCodes));
    }

}

