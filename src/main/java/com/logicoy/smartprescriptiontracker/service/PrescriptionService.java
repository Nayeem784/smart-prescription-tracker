package com.logicoy.smartprescriptiontracker.service;

import com.logicoy.smartprescriptiontracker.dto.PrescriptionRequestDTO;
import com.logicoy.smartprescriptiontracker.dto.PrescriptionResponseDTO;

import java.util.List;

public interface PrescriptionService {

    PrescriptionResponseDTO createPrescription(PrescriptionRequestDTO dto);

    List<PrescriptionResponseDTO> getPrescriptionsByPatient(Long patientId);
}
