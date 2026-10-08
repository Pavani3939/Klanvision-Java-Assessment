package com.klanvision.healthcare.service;

import com.klanvision.healthcare.dto.AppointmentBookingRequest;
import com.klanvision.healthcare.model.Appointment;
import com.klanvision.healthcare.model.AppointmentStatus;
import com.klanvision.healthcare.model.Doctor;
import com.klanvision.healthcare.model.Patient;
import com.klanvision.healthcare.repository.AppointmentRepository;
import com.klanvision.healthcare.repository.DoctorRepository;
import com.klanvision.healthcare.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class HealthcareService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    public HealthcareService(PatientRepository patientRepository,
                             DoctorRepository doctorRepository,
                             AppointmentRepository appointmentRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
    }

    // --- Patient Operations ---
    @Transactional
    public Patient registerPatient(Patient patient) {
        if (patientRepository.existsByEmail(patient.getEmail())) {
            throw new IllegalArgumentException("Patient with email " + patient.getEmail() + " already exists.");
        }
        return patientRepository.save(patient);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with ID: " + id));
    }

    // --- Doctor Operations ---
    @Transactional
    public Doctor addDoctor(Doctor doctor) {
        if (doctor.getEmail() != null && doctorRepository.findByEmail(doctor.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Doctor with email " + doctor.getEmail() + " already exists.");
        }
        return doctorRepository.save(doctor);
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public List<Doctor> searchDoctors(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllDoctors();
        }
        return doctorRepository.searchByNameOrSpecialization(query.trim());
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found with ID: " + id));
    }

    // --- Appointment Operations ---
    @Transactional
    public Appointment bookAppointment(AppointmentBookingRequest request) {
        Patient patient = getPatientById(request.getPatientId());
        Doctor doctor = getDoctorById(request.getDoctorId());

        if (request.getAppointmentDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Appointment date and time must be in the future.");
        }

        // Duplicate slot validation
        List<Appointment> conflicts = appointmentRepository.findConflictingAppointments(
                doctor.getId(), request.getAppointmentDateTime());

        if (!conflicts.isEmpty()) {
            throw new IllegalStateException("Doctor " + doctor.getName() + " is already booked for " + request.getAppointmentDateTime());
        }

        Appointment appointment = new Appointment(patient, doctor, request.getAppointmentDateTime(), request.getNotes());
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> getAppointmentsForPatient(Long patientId) {
        getPatientById(patientId); // Validate existence
        return appointmentRepository.findByPatientIdOrderByAppointmentDateTimeDesc(patientId);
    }

    public List<Appointment> getAppointmentsForDoctor(Long doctorId) {
        getDoctorById(doctorId); // Validate existence
        return appointmentRepository.findByDoctorIdOrderByAppointmentDateTimeDesc(doctorId);
    }

    @Transactional
    public Appointment cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + appointmentId));
        
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("Appointment is already cancelled.");
        }
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("Completed appointments cannot be cancelled.");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        return appointmentRepository.save(appointment);
    }

    @Transactional
    public Appointment updateAppointmentStatus(Long appointmentId, AppointmentStatus status, LocalDateTime newTime) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + appointmentId));

        if (status == AppointmentStatus.RESCHEDULED) {
            if (newTime == null || newTime.isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Valid future date/time is required for rescheduling.");
            }
            List<Appointment> conflicts = appointmentRepository.findConflictingAppointments(
                    appointment.getDoctor().getId(), newTime);
            if (!conflicts.isEmpty() && !conflicts.get(0).getId().equals(appointmentId)) {
                throw new IllegalStateException("Doctor is already booked at the requested rescheduled time.");
            }
            appointment.setAppointmentDateTime(newTime);
        }

        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAppointmentHistory() {
        return appointmentRepository.findByStatusInOrderByAppointmentDateTimeDesc(
                Arrays.asList(AppointmentStatus.COMPLETED, AppointmentStatus.CANCELLED)
        );
    }
}
