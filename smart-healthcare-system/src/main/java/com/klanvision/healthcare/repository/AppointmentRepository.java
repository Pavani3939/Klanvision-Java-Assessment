package com.klanvision.healthcare.repository;

import com.klanvision.healthcare.model.Appointment;
import com.klanvision.healthcare.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    List<Appointment> findByPatientIdOrderByAppointmentDateTimeDesc(Long patientId);
    
    List<Appointment> findByDoctorIdOrderByAppointmentDateTimeDesc(Long doctorId);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.appointmentDateTime = :dateTime AND a.status IN ('SCHEDULED', 'RESCHEDULED')")
    List<Appointment> findConflictingAppointments(@Param("doctorId") Long doctorId, @Param("dateTime") LocalDateTime dateTime);

    List<Appointment> findByStatusInOrderByAppointmentDateTimeDesc(List<AppointmentStatus> statuses);

    List<Appointment> findByPatientIdAndStatusIn(Long patientId, List<AppointmentStatus> statuses);

    List<Appointment> findByDoctorIdAndStatusIn(Long doctorId, List<AppointmentStatus> statuses);
}
