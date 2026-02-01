package com.logicoy.smartprescriptiontracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class PrescriptionRequestDTO {

    @NotBlank
    private String medication;

    @NotNull
    private LocalDate prescribedDate;

    @NotNull
    private Long patientId;

    @NotNull
    private Long doctorId;

    // getters & setters
    public String getMedication() { return medication; }
    public void setMedication(String medication) { this.medication = medication; }

    public LocalDate getPrescribedDate() { return prescribedDate; }
    public void setPrescribedDate(LocalDate prescribedDate) {
        this.prescribedDate = prescribedDate;
    }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
}
