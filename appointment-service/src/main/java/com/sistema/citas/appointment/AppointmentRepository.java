package com.sistema.citas.appointment;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByDoctorName(String doctorName);

    boolean existsByDoctorNameAndDateTimeAndStatusNot(
            String doctorName,
            LocalDateTime dateTime,
            AppointmentStatus status
    );

    boolean existsByPatientIdAndDateTimeAndStatusNot(
            Long patientId,
            LocalDateTime dateTime,
            AppointmentStatus status
    );
}
