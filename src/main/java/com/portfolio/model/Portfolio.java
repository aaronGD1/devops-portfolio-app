package com.portfolio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "portfolio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String fullName;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    private String phone;

    @NotBlank(message = "College name is required")
    private String collegeName;

    @DecimalMin(value = "0.0") @DecimalMax(value = "10.0")
    private Double cgpa;

    private String degree;
    private String branch;
    private Integer graduationYear;

    @Column(length = 1000)
    private String bio;

    private String linkedinUrl;
    private String githubUrl;
    private String portfolioUrl;

    // Stored as comma-separated values
    @Column(length = 2000)
    private String skills;

    @Column(length = 2000)
    private String projects;

    @Column(length = 1000)
    private String certifications;

    @Column(length = 1000)
    private String achievements;

    // File paths
    private String photoPath;
    private String resumePath;

    private String jobTitle;
    private String location;
}
