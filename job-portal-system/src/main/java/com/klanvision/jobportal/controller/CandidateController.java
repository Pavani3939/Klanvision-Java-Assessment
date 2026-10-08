package com.klanvision.jobportal.controller;

import com.klanvision.jobportal.model.Candidate;
import com.klanvision.jobportal.service.JobPortalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
@CrossOrigin(origins = "*")
public class CandidateController {

    private final JobPortalService jobPortalService;

    public CandidateController(JobPortalService jobPortalService) {
        this.jobPortalService = jobPortalService;
    }

    @PostMapping("/register")
    public ResponseEntity<Candidate> registerCandidate(@Valid @RequestBody Candidate candidate) {
        Candidate registered = jobPortalService.registerCandidate(candidate);
        return new ResponseEntity<>(registered, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Candidate>> getAllCandidates() {
        return ResponseEntity.ok(jobPortalService.getAllCandidates());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Candidate> getCandidateById(@PathVariable Long id) {
        return ResponseEntity.ok(jobPortalService.getCandidateById(id));
    }
}
