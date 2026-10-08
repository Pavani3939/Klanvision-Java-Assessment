package com.klanvision.jobportal;

import com.klanvision.jobportal.model.Candidate;
import com.klanvision.jobportal.model.Company;
import com.klanvision.jobportal.model.JobApplication;
import com.klanvision.jobportal.model.JobPosting;
import com.klanvision.jobportal.service.JobPortalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class JobPortalServiceTest {

    @Autowired
    private JobPortalService jobPortalService;

    @Test
    public void testJobPostingAndApplicationDuplicatePrevention() {
        Company comp = jobPortalService.registerCompany(new Company(null, "Test Corp", "hr@testcorp.com", "Remote", "Software"));
        Candidate cand = jobPortalService.registerCandidate(new Candidate(null, "Alice Developer", "alice@dev.com", "9998887777", "Java, Spring Boot", "B.Tech", 2));

        JobPosting job = new JobPosting("Senior Java Developer", "Spring Boot microservices", "Java 21, Spring Boot", 900000.0, "Remote", comp);
        JobPosting postedJob = jobPortalService.postJob(comp.getId(), job);

        assertNotNull(postedJob.getId());

        JobApplication app = jobPortalService.applyForJob(cand.getId(), postedJob.getId(), "Cover letter summary");
        assertNotNull(app.getId());

        // Test duplicate application prevention
        assertThrows(IllegalStateException.class, () -> jobPortalService.applyForJob(cand.getId(), postedJob.getId(), "Duplicate attempt"));
    }
}
