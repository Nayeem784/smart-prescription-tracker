package com.logicoy.smartprescriptiontracker.repository;

import com.logicoy.smartprescriptiontracker.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    @Query("""
        SELECT p FROM Prescription p
        WHERE p.patient.id = :patientId
    """)
    List<Prescription> findByPatientId(Long patientId);
}
