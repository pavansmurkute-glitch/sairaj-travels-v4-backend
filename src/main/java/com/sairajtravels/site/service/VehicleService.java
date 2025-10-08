package com.sairajtravels.site.service;

import com.sairajtravels.site.dto.VehicleDTO;
import com.sairajtravels.site.dto.VehicleTypeDTO;
import com.sairajtravels.site.entity.Vehicle;
import com.sairajtravels.site.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    // Convert Entity -> DTO
    private VehicleDTO convertToDTO(Vehicle vehicle) {
        VehicleDTO dto = new VehicleDTO();
        dto.setVehicleId(vehicle.getVehicleId());
        dto.setName(vehicle.getName());
        dto.setType(vehicle.getType());
        dto.setCapacity(vehicle.getCapacity());
        dto.setIsAC(vehicle.getIsAC());
        dto.setDescription(vehicle.getDescription());
        dto.setCreatedAt(vehicle.getCreatedAt());
        dto.setMainImageUrl(vehicle.getMainImageUrl()); // ✅ added new column
        return dto;
    }

    // Convert DTO -> Entity
    private Vehicle convertToEntity(VehicleDTO dto) {
        Vehicle v = new Vehicle();
        v.setVehicleId(dto.getVehicleId());
        v.setName(dto.getName());
        v.setType(dto.getType());
        v.setCapacity(dto.getCapacity());
        v.setIsAC(dto.getIsAC());
        v.setDescription(dto.getDescription());
        v.setCreatedAt(dto.getCreatedAt());
        v.setMainImageUrl(dto.getMainImageUrl()); // ✅ String
        return v;
    }

    // CRUD methods
    public List<VehicleDTO> getAllVehicles() {
        return vehicleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public VehicleDTO getVehicleById(Integer id) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(id);
        return vehicle.map(this::convertToDTO).orElse(null);
    }

    public VehicleDTO createVehicle(VehicleDTO dto) {
        Vehicle saved = vehicleRepository.save(convertToEntity(dto));
        return convertToDTO(saved);
    }

    public VehicleDTO updateVehicle(Integer id, VehicleDTO dto) {
        if (vehicleRepository.existsById(id)) {
            dto.setVehicleId(id);
            Vehicle updated = vehicleRepository.save(convertToEntity(dto));
            return convertToDTO(updated);
        }
        return null;
    }

    public void deleteVehicle(Integer id) {
        vehicleRepository.deleteById(id);
    }

    // Get unique vehicle types for dropdown
    public List<VehicleTypeDTO> getVehicleTypes() {
        return vehicleRepository.findAll().stream()
                .map(vehicle -> {
                    VehicleTypeDTO typeDTO = new VehicleTypeDTO();
                    typeDTO.setId(vehicle.getVehicleId().toString());
                    typeDTO.setName(vehicle.getName());
                    typeDTO.setCapacity(vehicle.getCapacity());
                    typeDTO.setType(vehicle.getType());
                    typeDTO.setIsAC(vehicle.getIsAC());
                    return typeDTO;
                })
                .collect(Collectors.toList());
    }
}
