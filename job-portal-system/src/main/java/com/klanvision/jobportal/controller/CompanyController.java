package com.klanvision.jobportal.controller;

import com.klanvision.jobportal.model.Company;
import com.klanvision.jobportal.service.JobPortalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@CrossOrigin(origins = "*")
public class CompanyController {

    private final JobPortalService jobPortalService;

    public CompanyController(JobPortalService jobPortalService) {
        this.jobPortalService = jobPortalService;
    }

    @PostMapping("/register")
    public ResponseEntity<Company> registerCompany(@Valid @RequestBody Company company) {
        Company registered = jobPortalService.registerCompany(company);
        return new ResponseEntity<>(registered, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(jobPortalService.getAllCompanies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        return ResponseEntity.ok(jobPortalService.getCompanyById(id));
    }
}
