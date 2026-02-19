package com.logicoy.smartprescriptiontracker.controller;

import com.logicoy.smartprescriptiontracker.dto.PrescriptionRequestDTO;
import com.logicoy.smartprescriptiontracker.dto.PrescriptionResponseDTO;
import com.logicoy.smartprescriptiontracker.service.PrescriptionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PrescriptionController
 *
 * Exposes REST APIs related to prescription management.
 * Acts as an entry point for client requests and delegates
 * business logic to the service layer.
 */
@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    // Logger for request-level tracing
    private static final Logger log =
            LoggerFactory.getLogger(PrescriptionController.class);

    private final PrescriptionService prescriptionService;

    /**
     * Constructor-based dependency injection.
     */
    public PrescriptionController(
            PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    /**
     * Creates a new prescription.
     *
     * @param dto validated request body
     * @return created prescription with HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<PrescriptionResponseDTO> create(
            @Valid @RequestBody PrescriptionRequestDTO dto) {

        // Log API invocation
        log.info("POST /api/prescriptions called");

        PrescriptionResponseDTO response =
                prescriptionService.createPrescription(dto);

        // Log successful response
        log.info(
            "Prescription created successfully with id={}",
            response.getId()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Fetches all prescriptions for a given patient.
     *
     * @param patientId patient identifier
     * @return list of prescriptions for the patient
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PrescriptionResponseDTO>> getByPatient(
            @PathVariable Long patientId) {

        // Log API invocation with path variable
        log.info(
            "GET /api/prescriptions/patient/{} called",
            patientId
        );

        List<PrescriptionResponseDTO> prescriptions =
                prescriptionService.getPrescriptionsByPatient(patientId);

        // Log result size for observability
        log.info(
            "Found {} prescriptions for patientId={}",
            prescriptions.size(),
            patientId
        );

        return ResponseEntity.ok(prescriptions);
    }
}
