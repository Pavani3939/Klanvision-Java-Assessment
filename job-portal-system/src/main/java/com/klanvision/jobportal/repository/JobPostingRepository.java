package com.klanvision.jobportal.repository;

import com.klanvision.jobportal.model.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    List<JobPosting> findByActiveTrue();

    @Query("SELECT j FROM JobPosting j WHERE j.active = true AND (" +
           "LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(j.requiredSkills) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(j.location) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(j.company.companyName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<JobPosting> searchJobs(@Param("keyword") String keyword);

    List<JobPosting> findByCompanyId(Long companyId);
}
