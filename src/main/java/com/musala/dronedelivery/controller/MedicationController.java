package com.musala.dronedelivery.controller;

import com.musala.dronedelivery.dto.medication.MedicationCreationDTO;
import com.musala.dronedelivery.dto.medication.MedicationDTO;
import com.musala.dronedelivery.exception.AppException;
import com.musala.dronedelivery.service.MedicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medications")
@RequiredArgsConstructor
public class MedicationController {

    private final MedicationService medicationService;

    @PostMapping
    public ResponseEntity<MedicationDTO> createMedication(@RequestBody @Valid MedicationCreationDTO medicationDTO) throws AppException {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicationService.save(medicationDTO));
    }

    @GetMapping
    public ResponseEntity<List<MedicationCreationDTO>> getMedications() {
        return ResponseEntity.ok(medicationService.findAll());
    }

}
