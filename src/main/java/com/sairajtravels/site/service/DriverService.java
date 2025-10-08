package com.sairajtravels.site.service;

import com.sairajtravels.site.dto.DriverDTO;
import com.sairajtravels.site.entity.Driver;
import com.sairajtravels.site.repository.DriverRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    // ======================
    // CRUD Methods
    // ======================

    public List<DriverDTO> getAllDrivers() {
        return driverRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public DriverDTO getDriverById(Integer id) {
        return driverRepository.findById(id)
                .map(this::mapToDTO)
                .orElse(null);
    }

    public DriverDTO addDriver(DriverDTO driverDTO) {
        Driver driver = mapToEntity(driverDTO);
        Driver saved = driverRepository.save(driver);
        return mapToDTO(saved);
    }

    public DriverDTO updateDriver(Integer id, DriverDTO driverDTO) {
        return driverRepository.findById(id).map(driver -> {
            driver.setFullName(driverDTO.getFullName());
            driver.setExperienceYears(driverDTO.getExperienceYears());
            driver.setLicenseType(driverDTO.getLicenseType());
            driver.setLanguages(driverDTO.getLanguages());
            driver.setDescription(driverDTO.getDescription());
            driver.setPhotoPath(driverDTO.getPhotoPath());

            // New fields
            driver.setLicenseNumber(driverDTO.getLicenseNumber());
            driver.setLicenseExpiryDate(driverDTO.getLicenseExpiryDate());
            driver.setPoliceVerified(driverDTO.getPoliceVerified());
            driver.setAadhaarVerified(driverDTO.getAadhaarVerified());
            driver.setSafetyTraining(driverDTO.getSafetyTraining());
            driver.setLanguagesSpoken(driverDTO.getLanguagesSpoken());
            driver.setEmergencyContact(driverDTO.getEmergencyContact());
            driver.setRating(driverDTO.getRating());

            return mapToDTO(driverRepository.save(driver));
        }).orElse(null);
    }

    public void deleteDriver(Integer id) {
        driverRepository.deleteById(id);
    }

    // ======================
    // Mappers
    // ======================

    private DriverDTO mapToDTO(Driver driver) {
        DriverDTO dto = new DriverDTO();
        dto.setDriverId(driver.getDriverId());
        dto.setFullName(driver.getFullName());
        dto.setExperienceYears(driver.getExperienceYears());
        dto.setLicenseType(driver.getLicenseType());
        dto.setLanguages(driver.getLanguages());
        dto.setDescription(driver.getDescription());
        dto.setPhotoPath(driver.getPhotoPath());

        // New fields
        dto.setLicenseNumber(driver.getLicenseNumber());
        dto.setLicenseExpiryDate(driver.getLicenseExpiryDate());
        dto.setPoliceVerified(driver.getPoliceVerified());
        dto.setAadhaarVerified(driver.getAadhaarVerified());
        dto.setSafetyTraining(driver.getSafetyTraining());
        dto.setLanguagesSpoken(driver.getLanguagesSpoken());
        dto.setEmergencyContact(driver.getEmergencyContact());
        dto.setRating(driver.getRating());

        return dto;
    }

    private Driver mapToEntity(DriverDTO dto) {
        Driver driver = new Driver();
        driver.setDriverId(dto.getDriverId());
        driver.setFullName(dto.getFullName());
        driver.setExperienceYears(dto.getExperienceYears());
        driver.setLicenseType(dto.getLicenseType());
        driver.setLanguages(dto.getLanguages());
        driver.setDescription(dto.getDescription());
        driver.setPhotoPath(dto.getPhotoPath());

        // New fields
        driver.setLicenseNumber(dto.getLicenseNumber());
        driver.setLicenseExpiryDate(dto.getLicenseExpiryDate());
        driver.setPoliceVerified(dto.getPoliceVerified());
        driver.setAadhaarVerified(dto.getAadhaarVerified());
        driver.setSafetyTraining(dto.getSafetyTraining());
        driver.setLanguagesSpoken(dto.getLanguagesSpoken());
        driver.setEmergencyContact(dto.getEmergencyContact());
        driver.setRating(dto.getRating());

        return driver;
    }
}
