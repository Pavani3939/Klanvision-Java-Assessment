package com.klanvision.healthcare.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Doctor name is required")
    private String name;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    @NotNull(message = "Years of experience is required")
    @Min(value = 0, message = "Experience must be non-negative")
    private Integer experience;

    @NotBlank(message = "Contact number is required")
    private String contactNumber;

    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;

    public Doctor() {}

    public Doctor(Long id, String name, String specialization, Integer experience, String contactNumber, String email) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.experience = experience;
        this.contactNumber = contactNumber;
        this.email = email;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public Integer getExperience() { return experience; }
    public void setExperience(Integer experience) { this.experience = experience; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
