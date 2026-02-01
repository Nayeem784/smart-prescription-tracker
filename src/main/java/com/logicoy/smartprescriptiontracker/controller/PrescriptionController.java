package com.logicoy.smartprescriptiontracker.controller;

import com.logicoy.smartprescriptiontracker.dto.PrescriptionRequestDTO;
import com.logicoy.smartprescriptiontracker.dto.PrescriptionResponseDTO;
import com.logicoy.smartprescriptiontracker.service.PrescriptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping
    public ResponseEntity<PrescriptionResponseDTO> create(
            @Valid @RequestBody PrescriptionRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(prescriptionService.createPrescription(dto));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PrescriptionResponseDTO>> getByPatient(
            @PathVariable Long patientId) {

        return ResponseEntity.ok(
                prescriptionService.getPrescriptionsByPatient(patientId));
    }
}
