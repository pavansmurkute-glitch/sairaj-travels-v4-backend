package com.sairajtravels.site.service;

import com.sairajtravels.site.dto.VehicleImageDTO;
import com.sairajtravels.site.entity.Vehicle;
import com.sairajtravels.site.entity.VehicleImage;
import com.sairajtravels.site.repository.VehicleImageRepository;
import com.sairajtravels.site.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleImageService {

    private final VehicleImageRepository vehicleImageRepository;
    private final VehicleRepository vehicleRepository;

    public VehicleImageService(VehicleImageRepository vehicleImageRepository, VehicleRepository vehicleRepository) {
        this.vehicleImageRepository = vehicleImageRepository;
        this.vehicleRepository = vehicleRepository;
    }

    private VehicleImageDTO convertToDTO(VehicleImage img) {
        return new VehicleImageDTO(
                img.getImageId(),
                img.getVehicle() != null ? img.getVehicle().getVehicleId() : null,
                img.getImageUrl()
        );
    }

    private VehicleImage convertToEntity(VehicleImageDTO dto) {
        VehicleImage img = new VehicleImage();
        img.setImageId(dto.getImageId());
        img.setImageUrl(dto.getImageUrl());

        if (dto.getVehicleId() != null) {
            Optional<Vehicle> vehicle = vehicleRepository.findById(dto.getVehicleId());
            vehicle.ifPresent(img::setVehicle);
        }
        return img;
    }

    public List<VehicleImageDTO> getAllImages() {
        return vehicleImageRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<VehicleImageDTO> getImagesByVehicle(Integer vehicleId) {
        return vehicleImageRepository.findByVehicle_VehicleId(vehicleId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public VehicleImageDTO createImage(VehicleImageDTO dto) {
        VehicleImage saved = vehicleImageRepository.save(convertToEntity(dto));
        return convertToDTO(saved);
    }

    public VehicleImageDTO updateImage(Integer id, VehicleImageDTO dto) {
        if (vehicleImageRepository.existsById(id)) {
            dto.setImageId(id);
            VehicleImage updated = vehicleImageRepository.save(convertToEntity(dto));
            return convertToDTO(updated);
        }
        return null;
    }

    public void deleteImage(Integer imageId) {
        vehicleImageRepository.deleteById(imageId);
    }
}
