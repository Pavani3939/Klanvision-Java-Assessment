package com.klanvision.jobportal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "candidates")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Candidate name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @NotBlank(message = "Skills are required")
    private String skills;

    @NotBlank(message = "Education details are required")
    private String education;

    @NotNull(message = "Years of experience is required")
    @Min(value = 0, message = "Experience cannot be negative")
    private Integer experience;

    public Candidate() {}

    public Candidate(Long id, String name, String email, String phone, String skills, String education, Integer experience) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.skills = skills;
        this.education = education;
        this.experience = experience;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }

    public Integer getExperience() { return experience; }
    public void setExperience(Integer experience) { this.experience = experience; }
}
