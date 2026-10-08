package com.klanvision.jobportal.config;

import com.klanvision.jobportal.model.Candidate;
import com.klanvision.jobportal.model.Company;
import com.klanvision.jobportal.model.JobPosting;
import com.klanvision.jobportal.service.JobPortalService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initJobPortalData(JobPortalService jobPortalService) {
        return args -> {
            // Pre-load Companies
            Company c1 = jobPortalService.registerCompany(new Company(null, "Klanvision Group Industries", "hr@klanvision.com", "Hyderabad, India", "Technology & AI Solutions"));
            Company c2 = jobPortalService.registerCompany(new Company(null, "TechNexus Global", "careers@technexus.com", "Bengaluru, India", "Enterprise Software"));

            // Pre-load Candidates
            Candidate cand1 = jobPortalService.registerCandidate(new Candidate(null, "Pavani Yellaturu", "pavani.dev@example.com", "+91 9876543210", "Java, Spring Boot, React, MySQL, REST API", "B.Tech Computer Science", 1));
            Candidate cand2 = jobPortalService.registerCandidate(new Candidate(null, "Aarav Gupta", "aarav@example.com", "+91 9123450000", "Java, Python, Microservices", "B.E Information Technology", 3));

            // Pre-load Jobs
            JobPosting j1 = new JobPosting("Java Full Stack Developer Intern", "Build modern enterprise web applications using Spring Boot and HTML/CSS/JS.", "Java 21, Spring Boot, REST API, HTML/CSS/JS", 450000.0, "Hyderabad / Remote", c1);
            jobPortalService.postJob(c1.getId(), j1);

            JobPosting j2 = new JobPosting("Backend Systems Engineer", "Design scalable microservices and database schemas.", "Java, Spring Cloud, PostgreSQL, Docker", 850000.0, "Bengaluru", c2);
            jobPortalService.postJob(c2.getId(), j2);

            // Pre-load Application
            jobPortalService.applyForJob(cand1.getId(), j1.getId(), "Experienced with Spring Boot REST API development and front-end integration.");
        };
    }
}
