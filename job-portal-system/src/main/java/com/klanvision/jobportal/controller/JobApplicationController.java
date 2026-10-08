package com.klanvision.jobportal.controller;

import com.klanvision.jobportal.model.ApplicationStatus;
import com.klanvision.jobportal.model.JobApplication;
import com.klanvision.jobportal.service.JobPortalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*")
public class JobApplicationController {

    private final JobPortalService jobPortalService;

    public JobApplicationController(JobPortalService jobPortalService) {
        this.jobPortalService = jobPortalService;
    }

    @PostMapping("/apply")
    public ResponseEntity<JobApplication> applyForJob(
            @RequestParam Long candidateId,
            @RequestParam Long jobId,
            @RequestParam(required = false) String resumeSummary) {
        JobApplication application = jobPortalService.applyForJob(candidateId, jobId, resumeSummary);
        return new ResponseEntity<>(application, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<JobApplication>> getAllApplications() {
        return ResponseEntity.ok(jobPortalService.getAllApplications());
    }

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<List<JobApplication>> getApplicationsForCandidate(@PathVariable Long candidateId) {
        return ResponseEntity.ok(jobPortalService.getApplicationsForCandidate(candidateId));
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<JobApplication>> getApplicationsForJob(@PathVariable Long jobId) {
        return ResponseEntity.ok(jobPortalService.getApplicationsForJob(jobId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<JobApplication> updateStatus(
            @PathVariable Long id,
            @RequestParam ApplicationStatus status) {
        return ResponseEntity.ok(jobPortalService.updateApplicationStatus(id, status));
    }
}
