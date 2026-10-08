package com.klanvision.jobportal.repository;

import com.klanvision.jobportal.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    
    Optional<JobApplication> findByCandidateIdAndJobPostingId(Long candidateId, Long jobPostingId);
    
    boolean existsByCandidateIdAndJobPostingId(Long candidateId, Long jobPostingId);

    List<JobApplication> findByCandidateId(Long candidateId);

    List<JobApplication> findByJobPostingId(Long jobPostingId);
}
