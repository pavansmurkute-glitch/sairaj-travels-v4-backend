package com.sairajtravels.site.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "drivers")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "driver_id")
    private Integer driverId;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "experience_years", nullable = false)
    private Integer experienceYears;

    @Column(name = "license_type", nullable = false, length = 50)
    private String licenseType;

    @Column(name = "languages", length = 200)
    private String languages;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "photo_path", length = 200)
    private String photoPath;

    // 🔹 New fields as per DB
    @Column(name = "license_number", length = 50)
    private String licenseNumber;

    @Column(name = "license_expiry_date")
    private LocalDate licenseExpiryDate;

    @Column(name = "police_verified")
    private Boolean policeVerified;

    @Column(name = "aadhaar_verified")
    private Boolean aadhaarVerified;

    @Column(name = "safety_training", length = 255)
    private String safetyTraining;

    @Column(name = "languages_spoken", length = 255)
    private String languagesSpoken;

    @Column(name = "emergency_contact", length = 20)
    private String emergencyContact;

    @Column(name = "rating")
    private Double rating;

    // ======================
    // Getters & Setters
    // ======================

    public Integer getDriverId() {
        return driverId;
    }
    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }
    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getLicenseType() {
        return licenseType;
    }
    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public String getLanguages() {
        return languages;
    }
    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoPath() {
        return photoPath;
    }
    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }
    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public LocalDate getLicenseExpiryDate() {
        return licenseExpiryDate;
    }
    public void setLicenseExpiryDate(LocalDate licenseExpiryDate) {
        this.licenseExpiryDate = licenseExpiryDate;
    }

    public Boolean getPoliceVerified() {
        return policeVerified;
    }
    public void setPoliceVerified(Boolean policeVerified) {
        this.policeVerified = policeVerified;
    }

    public Boolean getAadhaarVerified() {
        return aadhaarVerified;
    }
    public void setAadhaarVerified(Boolean aadhaarVerified) {
        this.aadhaarVerified = aadhaarVerified;
    }

    public String getSafetyTraining() {
        return safetyTraining;
    }
    public void setSafetyTraining(String safetyTraining) {
        this.safetyTraining = safetyTraining;
    }

    public String getLanguagesSpoken() {
        return languagesSpoken;
    }
    public void setLanguagesSpoken(String languagesSpoken) {
        this.languagesSpoken = languagesSpoken;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }
    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public Double getRating() {
        return rating;
    }
    public void setRating(Double rating) {
        this.rating = rating;
    }
}
