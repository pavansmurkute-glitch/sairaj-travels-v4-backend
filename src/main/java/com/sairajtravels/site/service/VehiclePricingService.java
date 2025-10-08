package com.sairajtravels.site.service;

import com.sairajtravels.site.dto.VehiclePricingDTO;
import com.sairajtravels.site.entity.Vehicle;
import com.sairajtravels.site.entity.VehiclePricing;
import com.sairajtravels.site.repository.VehiclePricingRepository;
import com.sairajtravels.site.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehiclePricingService {

    private final VehiclePricingRepository vehiclePricingRepository;
    private final VehicleRepository vehicleRepository;

    public VehiclePricingService(VehiclePricingRepository vehiclePricingRepository, VehicleRepository vehicleRepository) {
        this.vehiclePricingRepository = vehiclePricingRepository;
        this.vehicleRepository = vehicleRepository;
    }

    private VehiclePricingDTO convertToDTO(VehiclePricing pricing) {
        return new VehiclePricingDTO(
                pricing.getPricingId(),
                pricing.getVehicle() != null ? pricing.getVehicle().getVehicleId() : null,
                pricing.getRateType(),
                pricing.getRatePerKm(),
                pricing.getMinKmPerDay(),
                pricing.getPackageHours(),
                pricing.getPackageKm(),
                pricing.getPackageRate(),
                pricing.getExtraKmRate(),
                pricing.getExtraHourRate()
        );
    }

    private VehiclePricing convertToEntity(VehiclePricingDTO dto) {
        VehiclePricing pricing = new VehiclePricing();
        pricing.setPricingId(dto.getPricingId());
        pricing.setRateType(dto.getRateType());
        pricing.setRatePerKm(dto.getRatePerKm());
        pricing.setMinKmPerDay(dto.getMinKmPerDay());
        pricing.setPackageHours(dto.getPackageHours());
        pricing.setPackageKm(dto.getPackageKm());
        pricing.setPackageRate(dto.getPackageRate());
        pricing.setExtraKmRate(dto.getExtraKmRate());
        pricing.setExtraHourRate(dto.getExtraHourRate());

        if (dto.getVehicleId() != null) {
            Optional<Vehicle> vehicle = vehicleRepository.findById(dto.getVehicleId());
            vehicle.ifPresent(pricing::setVehicle);
        }

        return pricing;
    }

    public List<VehiclePricingDTO> getAllPricing() {
        return vehiclePricingRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<VehiclePricingDTO> getPricingByVehicle(Integer vehicleId) {
        System.out.println("🔍 VehiclePricingService: getPricingByVehicle called with vehicleId: " + vehicleId);
        try {
            System.out.println("🔍 VehiclePricingService: Repository instance: " + (vehiclePricingRepository != null ? "OK" : "NULL"));
            System.out.println("🔍 VehiclePricingService: Attempting to find pricing for vehicle ID: " + vehicleId);
            
            List<VehiclePricing> pricingList = vehiclePricingRepository.findByVehicle_VehicleId(vehicleId);
            System.out.println("🔍 VehiclePricingService: Found " + pricingList.size() + " pricing records from database");
            
            List<VehiclePricingDTO> result = pricingList.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            
            System.out.println("✅ VehiclePricingService: Successfully converted " + result.size() + " pricing records to DTOs");
            return result;
        } catch (Exception e) {
            System.err.println("❌ VehiclePricingService Error: " + e.getMessage());
            System.err.println("❌ VehiclePricingService Error Class: " + e.getClass().getName());
            System.err.println("❌ VehiclePricingService Error Stack Trace:");
            e.printStackTrace();
            throw e;
        }
    }

    public VehiclePricingDTO createPricing(VehiclePricingDTO dto) {
        VehiclePricing saved = vehiclePricingRepository.save(convertToEntity(dto));
        return convertToDTO(saved);
    }

    public VehiclePricingDTO updatePricing(Integer id, VehiclePricingDTO dto) {
        if (vehiclePricingRepository.existsById(id)) {
            dto.setPricingId(id);
            VehiclePricing updated = vehiclePricingRepository.save(convertToEntity(dto));
            return convertToDTO(updated);
        }
        return null;
    }

    public void deletePricing(Integer id) {
        vehiclePricingRepository.deleteById(id);
    }
}
