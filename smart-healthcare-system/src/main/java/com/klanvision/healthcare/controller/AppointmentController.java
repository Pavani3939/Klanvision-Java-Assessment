package com.klanvision.healthcare.controller;

import com.klanvision.healthcare.dto.AppointmentBookingRequest;
import com.klanvision.healthcare.model.Appointment;
import com.klanvision.healthcare.model.AppointmentStatus;
import com.klanvision.healthcare.service.HealthcareService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    private final HealthcareService healthcareService;

    public AppointmentController(HealthcareService healthcareService) {
        this.healthcareService = healthcareService;
    }

    @PostMapping("/book")
    public ResponseEntity<Appointment> bookAppointment(@Valid @RequestBody AppointmentBookingRequest request) {
        Appointment appointment = healthcareService.bookAppointment(request);
        return new ResponseEntity<>(appointment, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(healthcareService.getAllAppointments());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Appointment>> getAppointmentsForPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(healthcareService.getAppointmentsForPatient(patientId));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Appointment>> getAppointmentsForDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(healthcareService.getAppointmentsForDoctor(doctorId));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Appointment> cancelAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(healthcareService.cancelAppointment(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Appointment> updateStatus(
            @PathVariable Long id,
            @RequestParam AppointmentStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newTime) {
        return ResponseEntity.ok(healthcareService.updateAppointmentStatus(id, status, newTime));
    }

    @GetMapping("/history")
    public ResponseEntity<List<Appointment>> getAppointmentHistory() {
        return ResponseEntity.ok(healthcareService.getAppointmentHistory());
    }
}
