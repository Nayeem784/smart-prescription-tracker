package com.logicoy.smartprescriptiontracker.dto;

import java.time.LocalDate;

public class PrescriptionResponseDTO {

    private Long id;
    private String medication;
    private LocalDate prescribedDate;
    private String patientName;
    private String doctorName;

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMedication() { return medication; }
    public void setMedication(String medication) { this.medication = medication; }

    public LocalDate getPrescribedDate() { return prescribedDate; }
    public void setPrescribedDate(LocalDate prescribedDate) {
        this.prescribedDate = prescribedDate;
    }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
}
