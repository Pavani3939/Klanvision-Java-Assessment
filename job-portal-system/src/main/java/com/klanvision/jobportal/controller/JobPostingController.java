package com.klanvision.jobportal.controller;

import com.klanvision.jobportal.model.JobPosting;
import com.klanvision.jobportal.service.JobPortalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "*")
public class JobPostingController {

    private final JobPortalService jobPortalService;

    public JobPostingController(JobPortalService jobPortalService) {
        this.jobPortalService = jobPortalService;
    }

    @PostMapping("/post")
    public ResponseEntity<JobPosting> postJob(@RequestParam Long companyId, @Valid @RequestBody JobPosting jobPosting) {
        JobPosting created = jobPortalService.postJob(companyId, jobPosting);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<JobPosting>> getAllActiveJobs() {
        return ResponseEntity.ok(jobPortalService.getAllActiveJobs());
    }

    @GetMapping("/search")
    public ResponseEntity<List<JobPosting>> searchJobs(@RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(jobPortalService.searchJobs(keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobPosting> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobPortalService.getJobById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobPosting> updateJob(@PathVariable Long id, @Valid @RequestBody JobPosting updatedDetails) {
        return ResponseEntity.ok(jobPortalService.updateJob(id, updatedDetails));
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<JobPosting> closeJob(@PathVariable Long id) {
        return ResponseEntity.ok(jobPortalService.closeJob(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobPortalService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }
}
