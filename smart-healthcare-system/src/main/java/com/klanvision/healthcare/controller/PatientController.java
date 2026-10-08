package com.klanvision.healthcare.controller;

import com.klanvision.healthcare.model.Patient;
import com.klanvision.healthcare.service.HealthcareService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
public class PatientController {

    private final HealthcareService healthcareService;

    public PatientController(HealthcareService healthcareService) {
        this.healthcareService = healthcareService;
    }

    @PostMapping("/register")
    public ResponseEntity<Patient> registerPatient(@Valid @RequestBody Patient patient) {
        Patient registered = healthcareService.registerPatient(patient);
        return new ResponseEntity<>(registered, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(healthcareService.getAllPatients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(healthcareService.getPatientById(id));
    }
}
