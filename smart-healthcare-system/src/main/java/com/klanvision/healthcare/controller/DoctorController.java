package com.klanvision.healthcare.controller;

import com.klanvision.healthcare.model.Doctor;
import com.klanvision.healthcare.service.HealthcareService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "*")
public class DoctorController {

    private final HealthcareService healthcareService;

    public DoctorController(HealthcareService healthcareService) {
        this.healthcareService = healthcareService;
    }

    @PostMapping
    public ResponseEntity<Doctor> addDoctor(@Valid @RequestBody Doctor doctor) {
        Doctor saved = healthcareService.addDoctor(doctor);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return ResponseEntity.ok(healthcareService.getAllDoctors());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Doctor>> searchDoctors(@RequestParam(required = false) String query) {
        return ResponseEntity.ok(healthcareService.searchDoctors(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(healthcareService.getDoctorById(id));
    }
}
