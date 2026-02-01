package com.logicoy.smartprescriptiontracker.service.impl;

import com.logicoy.smartprescriptiontracker.dto.PrescriptionRequestDTO;
import com.logicoy.smartprescriptiontracker.dto.PrescriptionResponseDTO;
import com.logicoy.smartprescriptiontracker.entity.*;
import com.logicoy.smartprescriptiontracker.exception.PrescriptionRepository;
import com.logicoy.smartprescriptiontracker.exception.ResourceNotFoundException;
import com.logicoy.smartprescriptiontracker.repository.*;
import com.logicoy.smartprescriptiontracker.service.PrescriptionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public PrescriptionServiceImpl(
            PrescriptionRepository prescriptionRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public PrescriptionResponseDTO createPrescription(PrescriptionRequestDTO dto) {

        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient not found"));

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor not found"));

        Prescription prescription = new Prescription();
        prescription.setMedication(dto.getMedication());
        prescription.setPrescribedDate(dto.getPrescribedDate());
        prescription.setPatient(patient);
        prescription.setDoctor(doctor);

        return mapToResponse(prescriptionRepository.save(prescription));
    }

    @Override
    public List<PrescriptionResponseDTO> getPrescriptionsByPatient(Long patientId) {
        return prescriptionRepository.findByPatientId(patientId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private PrescriptionResponseDTO mapToResponse(Prescription p) {
        PrescriptionResponseDTO dto = new PrescriptionResponseDTO();
        dto.setId(p.getId());
        dto.setMedication(p.getMedication());
        dto.setPrescribedDate(p.getPrescribedDate());
        dto.setPatientName(p.getPatient().getName());
        dto.setDoctorName(p.getDoctor().getName());
        return dto;
    }
}
