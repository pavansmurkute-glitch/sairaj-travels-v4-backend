package com.sairajtravels.site.controller;

import com.sairajtravels.site.dto.VehicleDetailsDTO;
import com.sairajtravels.site.entity.Vehicle;
import com.sairajtravels.site.entity.VehicleCharges;
import com.sairajtravels.site.entity.VehiclePricing;
import com.sairajtravels.site.entity.VehicleTerm;
import com.sairajtravels.site.entity.VehicleImage;
import com.sairajtravels.site.repository.VehicleRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/vehicle-details")
public class VehicleDetailsController {

    private static final Logger logger = LoggerFactory.getLogger(VehicleDetailsController.class);

    @Autowired
    private VehicleRepository vehicleRepository;

    @PersistenceContext
    private EntityManager entityManager;

    // ✅ 1. Get all vehicles (basic info)
    @GetMapping
    public ResponseEntity<List<VehicleDetailsDTO>> getAllVehicles() {
        logger.info("🚗 VehicleDetailsController: getAllVehicles() called");
        try {
            List<Vehicle> vehicles = vehicleRepository.findAll();
            logger.info("📊 VehicleDetailsController: Found {} vehicles in database", vehicles.size());

            List<VehicleDetailsDTO> dtos = vehicles.stream().map(vehicle -> {
                VehicleDetailsDTO dto = new VehicleDetailsDTO();

                VehicleDetailsDTO.VehicleDTO vdto = new VehicleDetailsDTO.VehicleDTO();
                vdto.setVehicleId(vehicle.getVehicleId());
                vdto.setName(vehicle.getName());
                vdto.setType(vehicle.getType());
                vdto.setCapacity(vehicle.getCapacity());
                vdto.setIsAC(vehicle.getIsAC());
                vdto.setDescription(vehicle.getDescription());
                vdto.setMainImageUrl(vehicle.getMainImageUrl()); // ✅ Added here

                dto.setVehicle(vdto);
                return dto;
            }).toList();

            logger.info("✅ VehicleDetailsController: Successfully converted {} vehicles to DTOs", dtos.size());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            logger.error("❌ VehicleDetailsController: Error in getAllVehicles()", e);
            throw e;
        }
    }

    // ✅ 2. Get single vehicle with full details
    @GetMapping("/{id}")
    public ResponseEntity<VehicleDetailsDTO> getVehicleFullDetails(@PathVariable Integer id) {
        logger.info("🚗 VehicleDetailsController: getVehicleFullDetails() called with ID: {}", id);
        try {
            VehicleDetailsDTO dto = new VehicleDetailsDTO();

            // --- Vehicle ---
            logger.info("🔍 VehicleDetailsController: Looking for vehicle with ID: {}", id);
            Vehicle vehicle = vehicleRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.error("❌ VehicleDetailsController: Vehicle not found with ID: {}", id);
                        return new RuntimeException("Vehicle not found with ID: " + id);
                    });
            
            logger.info("✅ VehicleDetailsController: Found vehicle: {} (Type: {})", vehicle.getName(), vehicle.getType());
            
            VehicleDetailsDTO.VehicleDTO vdto = new VehicleDetailsDTO.VehicleDTO();
            vdto.setVehicleId(vehicle.getVehicleId());
            vdto.setName(vehicle.getName());
            vdto.setType(vehicle.getType());
            vdto.setCapacity(vehicle.getCapacity());
            vdto.setIsAC(vehicle.getIsAC());
            vdto.setDescription(vehicle.getDescription());
            vdto.setMainImageUrl(vehicle.getMainImageUrl()); // ✅ Added here

            dto.setVehicle(vdto);

            // --- Pricing ---
            logger.info("💰 VehicleDetailsController: Fetching pricing for vehicle ID: {}", id);
            TypedQuery<VehiclePricing> pricingQuery = entityManager.createQuery(
                    "SELECT p FROM VehiclePricing p WHERE p.vehicle.vehicleId = :id", VehiclePricing.class);
            pricingQuery.setParameter("id", id);

            List<VehicleDetailsDTO.VehiclePricingDTO> pricingDTOs = pricingQuery.getResultList().stream().map(p -> {
                VehicleDetailsDTO.VehiclePricingDTO pdto = new VehicleDetailsDTO.VehiclePricingDTO();
                pdto.setPricingId(p.getPricingId());
                pdto.setRatePerKm(p.getRatePerKm());
                pdto.setExtraKmRate(p.getExtraKmRate());
                pdto.setExtraHourRate(p.getExtraHourRate());
                pdto.setMinKmPerDay(p.getMinKmPerDay());
                pdto.setPackageRate(p.getPackageRate());
                pdto.setPackageKm(p.getPackageKm());
                pdto.setPackageHours(p.getPackageHours());
                pdto.setRateType(p.getRateType());
                return pdto;
            }).toList();
            logger.info("💰 VehicleDetailsController: Found {} pricing records for vehicle ID: {}", pricingDTOs.size(), id);
            dto.setPricing(pricingDTOs);

            // --- Charges ---
            logger.info("💳 VehicleDetailsController: Fetching charges for vehicle ID: {}", id);
            TypedQuery<VehicleCharges> chargesQuery = entityManager.createQuery(
                    "SELECT c FROM VehicleCharges c WHERE c.vehicle.vehicleId = :id", VehicleCharges.class);
            chargesQuery.setParameter("id", id);

            List<VehicleDetailsDTO.VehicleChargesDTO> chargeDTOs = chargesQuery.getResultList().stream().map(c -> {
                VehicleDetailsDTO.VehicleChargesDTO cdto = new VehicleDetailsDTO.VehicleChargesDTO();
                cdto.setChargeId(c.getChargeId());
                cdto.setDriverAllowance(c.getDriverAllowance());
                cdto.setTollIncluded(c.getTollIncluded());
                cdto.setParkingIncluded(c.getParkingIncluded());
                cdto.setFuelIncluded(c.getFuelIncluded());
                cdto.setNightCharge(c.getNightCharge());

                return cdto;
            }).toList();
            logger.info("💳 VehicleDetailsController: Found {} charge records for vehicle ID: {}", chargeDTOs.size(), id);
            dto.setCharges(chargeDTOs);

            // --- Terms ---
            logger.info("📋 VehicleDetailsController: Fetching terms for vehicle ID: {}", id);
            TypedQuery<VehicleTerm> termQuery = entityManager.createQuery(
                    "SELECT t FROM VehicleTerm t WHERE t.vehicle.vehicleId = :id", VehicleTerm.class);
            termQuery.setParameter("id", id);

            List<VehicleDetailsDTO.VehicleTermDTO> termDTOs = termQuery.getResultList().stream().map(t -> {
                VehicleDetailsDTO.VehicleTermDTO tdto = new VehicleDetailsDTO.VehicleTermDTO();
                tdto.setTermId(t.getTermId());
                tdto.setTermText(t.getTermText());
                return tdto;
            }).toList();
            logger.info("📋 VehicleDetailsController: Found {} term records for vehicle ID: {}", termDTOs.size(), id);
            dto.setTerms(termDTOs);

            // --- Images ---
            logger.info("🖼️ VehicleDetailsController: Fetching images for vehicle ID: {}", id);
            TypedQuery<VehicleImage> imageQuery = entityManager.createQuery(
                    "SELECT i FROM VehicleImage i WHERE i.vehicle.vehicleId = :id", VehicleImage.class);
            imageQuery.setParameter("id", id);

            List<VehicleDetailsDTO.VehicleImageDTO> imgDTOs = imageQuery.getResultList().stream().map(i -> {
                VehicleDetailsDTO.VehicleImageDTO idto = new VehicleDetailsDTO.VehicleImageDTO();
                idto.setImageId(i.getImageId());
                idto.setImageUrl(i.getImageUrl());
                return idto;
            }).toList();
            logger.info("🖼️ VehicleDetailsController: Found {} image records for vehicle ID: {}", imgDTOs.size(), id);
            dto.setImages(imgDTOs);

            logger.info("✅ VehicleDetailsController: Successfully built complete vehicle details for ID: {}", id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("❌ VehicleDetailsController: Error in getVehicleFullDetails() for ID: {}", id, e);
            throw e;
        }
    }
}
