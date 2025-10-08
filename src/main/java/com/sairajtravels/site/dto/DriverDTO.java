package com.sairajtravels.site.dto;

import java.time.LocalDate;

public class DriverDTO {
    private Integer driverId;
    private String fullName;
    private Integer experienceYears;
    private String licenseType;
    private String languages;
    private String description;
    private String photoPath;

    // New fields (same as entity)
    private String licenseNumber;
    private LocalDate licenseExpiryDate;
    private Boolean policeVerified;
    private Boolean aadhaarVerified;
    private String safetyTraining;
    private String languagesSpoken;
    private String emergencyContact;
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
