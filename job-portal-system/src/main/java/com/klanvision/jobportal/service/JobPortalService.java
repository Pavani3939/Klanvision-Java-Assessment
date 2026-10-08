package com.klanvision.jobportal.service;

import com.klanvision.jobportal.model.*;
import com.klanvision.jobportal.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JobPortalService {

    private final CandidateRepository candidateRepository;
    private final CompanyRepository companyRepository;
    private final JobPostingRepository jobPostingRepository;
    private final JobApplicationRepository jobApplicationRepository;

    public JobPortalService(CandidateRepository candidateRepository,
                            CompanyRepository companyRepository,
                            JobPostingRepository jobPostingRepository,
                            JobApplicationRepository jobApplicationRepository) {
        this.candidateRepository = candidateRepository;
        this.companyRepository = companyRepository;
        this.jobPostingRepository = jobPostingRepository;
        this.jobApplicationRepository = jobApplicationRepository;
    }

    // --- Candidate Management ---
    @Transactional
    public Candidate registerCandidate(Candidate candidate) {
        if (candidateRepository.existsByEmail(candidate.getEmail())) {
            throw new IllegalArgumentException("Candidate with email " + candidate.getEmail() + " already exists.");
        }
        return candidateRepository.save(candidate);
    }

    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    public Candidate getCandidateById(Long id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found with ID: " + id));
    }

    // --- Company Management ---
    @Transactional
    public Company registerCompany(Company company) {
        if (companyRepository.existsByEmail(company.getEmail())) {
            throw new IllegalArgumentException("Company with email " + company.getEmail() + " already exists.");
        }
        return companyRepository.save(company);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Company not found with ID: " + id));
    }

    // --- Job Posting Management ---
    @Transactional
    public JobPosting postJob(Long companyId, JobPosting jobPosting) {
        Company company = getCompanyById(companyId);
        jobPosting.setCompany(company);
        jobPosting.setActive(true);
        return jobPostingRepository.save(jobPosting);
    }

    public List<JobPosting> getAllActiveJobs() {
        return jobPostingRepository.findByActiveTrue();
    }

    public List<JobPosting> searchJobs(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllActiveJobs();
        }
        return jobPostingRepository.searchJobs(keyword.trim());
    }

    public JobPosting getJobById(Long id) {
        return jobPostingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job posting not found with ID: " + id));
    }

    @Transactional
    public JobPosting updateJob(Long id, JobPosting updatedDetails) {
        JobPosting job = getJobById(id);
        job.setTitle(updatedDetails.getTitle());
        job.setDescription(updatedDetails.getDescription());
        job.setRequiredSkills(updatedDetails.getRequiredSkills());
        job.setSalary(updatedDetails.getSalary());
        job.setLocation(updatedDetails.getLocation());
        return jobPostingRepository.save(job);
    }

    @Transactional
    public JobPosting closeJob(Long id) {
        JobPosting job = getJobById(id);
        job.setActive(false);
        return jobPostingRepository.save(job);
    }

    @Transactional
    public void deleteJob(Long id) {
        JobPosting job = getJobById(id);
        jobPostingRepository.delete(job);
    }

    // --- Job Application Management ---
    @Transactional
    public JobApplication applyForJob(Long candidateId, Long jobId, String resumeSummary) {
        Candidate candidate = getCandidateById(candidateId);
        JobPosting job = getJobById(jobId);

        if (!job.getActive()) {
            throw new IllegalStateException("Cannot apply for a closed job posting.");
        }

        // Duplicate application check
        if (jobApplicationRepository.existsByCandidateIdAndJobPostingId(candidateId, jobId)) {
            throw new IllegalStateException("Candidate has already applied for this job posting.");
        }

        JobApplication application = new JobApplication(candidate, job, resumeSummary);
        return jobApplicationRepository.save(application);
    }

    public List<JobApplication> getApplicationsForCandidate(Long candidateId) {
        getCandidateById(candidateId);
        return jobApplicationRepository.findByCandidateId(candidateId);
    }

    public List<JobApplication> getApplicationsForJob(Long jobId) {
        getJobById(jobId);
        return jobApplicationRepository.findByJobPostingId(jobId);
    }

    public List<JobApplication> getAllApplications() {
        return jobApplicationRepository.findAll();
    }

    @Transactional
    public JobApplication updateApplicationStatus(Long applicationId, ApplicationStatus status) {
        JobApplication app = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Job application not found with ID: " + applicationId));
        app.setStatus(status);
        return jobApplicationRepository.save(app);
    }
}
