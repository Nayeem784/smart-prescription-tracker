package com.logicoy.smartprescriptiontracker.service.impl;

import com.logicoy.smartprescriptiontracker.dto.PrescriptionRequestDTO;
import com.logicoy.smartprescriptiontracker.dto.PrescriptionResponseDTO;
import com.logicoy.smartprescriptiontracker.entity.Doctor;
import com.logicoy.smartprescriptiontracker.entity.Patient;
import com.logicoy.smartprescriptiontracker.entity.Prescription;
import com.logicoy.smartprescriptiontracker.exception.ResourceNotFoundException;
import com.logicoy.smartprescriptiontracker.repository.DoctorRepository;
import com.logicoy.smartprescriptiontracker.repository.PatientRepository;
import com.logicoy.smartprescriptiontracker.repository.PrescriptionRepository;
import com.logicoy.smartprescriptiontracker.service.PrescriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * PrescriptionServiceImpl
 *
 * Implements business logic related to prescriptions.
 * Handles validation, entity retrieval, persistence,
 * and mapping to response DTOs.
 */
@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    // Logger for service-level business flow tracking
    private static final Logger log =
            LoggerFactory.getLogger(PrescriptionServiceImpl.class);

    private final PrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    /**
     * Constructor-based dependency injection.
     * Promotes immutability and testability.
     */
    public PrescriptionServiceImpl(
            PrescriptionRepository prescriptionRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository) {

        this.prescriptionRepository = prescriptionRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    /**
     * Creates a new prescription.
     *
     * @param dto request DTO containing prescription details
     * @return saved prescription response DTO
     */
    @Override
    public PrescriptionResponseDTO createPrescription(
            PrescriptionRequestDTO dto) {

        // Log incoming business request
        log.info(
            "Creating prescription for patientId={} and doctorId={}",
            dto.getPatientId(),
            dto.getDoctorId()
        );

        // Retrieve patient or throw exception if not found
        Patient patient = patientRepository
                .findById(dto.getPatientId())
                .orElseThrow(() -> {
                    log.warn(
                        "Patient not found with id={}",
                        dto.getPatientId()
                    );
                    return new ResourceNotFoundException(
                            "Patient not found"
                    );
                });

        // Retrieve doctor or throw exception if not found
        Doctor doctor = doctorRepository
                .findById(dto.getDoctorId())
                .orElseThrow(() -> {
                    log.warn(
                        "Doctor not found with id={}",
                        dto.getDoctorId()
                    );
                    return new ResourceNotFoundException(
                            "Doctor not found"
                    );
                });

        // Build Prescription entity
        Prescription prescription = new Prescription();
        prescription.setMedication(dto.getMedication());
        prescription.setPrescribedDate(dto.getPrescribedDate());
        prescription.setPatient(patient);
        prescription.setDoctor(doctor);

        // Persist prescription entity
        Prescription saved =
                prescriptionRepository.save(prescription);

        // Log successful creation
        log.info(
            "Prescription created successfully with id={}",
            saved.getId()
        );

        return mapToResponse(saved);
    }

    /**
     * Fetches prescriptions for a given patient.
     *
     * @param patientId patient identifier
     * @return list of prescription response DTOs
     */
    @Override
    public List<PrescriptionResponseDTO> getPrescriptionsByPatient(
            Long patientId) {

        // Log query operation
        log.info(
            "Fetching prescriptions for patientId={}",
            patientId
        );

        return prescriptionRepository
                .findByPatientId(patientId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Maps Prescription entity to response DTO.
     * Keeps persistence model hidden from API consumers.
     */
    private PrescriptionResponseDTO mapToResponse(
            Prescription prescription) {

        PrescriptionResponseDTO dto =
                new PrescriptionResponseDTO();

        dto.setId(prescription.getId());
        dto.setMedication(prescription.getMedication());
        dto.setPrescribedDate(
                prescription.getPrescribedDate()
        );
        dto.setPatientName(
                prescription.getPatient().getName()
        );
        dto.setDoctorName(
                prescription.getDoctor().getName()
        );

        return dto;
    }
}
