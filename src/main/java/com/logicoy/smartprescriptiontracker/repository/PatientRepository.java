package com.logicoy.smartprescriptiontracker.repository;

import com.logicoy.smartprescriptiontracker.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
