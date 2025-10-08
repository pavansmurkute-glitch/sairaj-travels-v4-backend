package com.sairajtravels.site.controller;

import com.sairajtravels.site.dto.VehiclePricingDTO;
import com.sairajtravels.site.service.VehiclePricingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicle-pricing")
public class VehiclePricingController {

    private final VehiclePricingService vehiclePricingService;

    public VehiclePricingController(VehiclePricingService vehiclePricingService) {
        this.vehiclePricingService = vehiclePricingService;
    }

    @GetMapping
    public List<VehiclePricingDTO> getAllPricing() {
        return vehiclePricingService.getAllPricing();
    }

    @GetMapping("/vehicle/{vehicleId}")
    public List<VehiclePricingDTO> getPricingByVehicle(@PathVariable Integer vehicleId) {
        System.out.println("🔍 VehiclePricingController: getPricingByVehicle called with vehicleId: " + vehicleId);
        try {
            List<VehiclePricingDTO> result = vehiclePricingService.getPricingByVehicle(vehicleId);
            System.out.println("✅ VehiclePricingController: Successfully retrieved " + result.size() + " pricing records for vehicle " + vehicleId);
            return result;
        } catch (Exception e) {
            System.err.println("❌ VehiclePricingController Error: " + e.getMessage());
            System.err.println("❌ VehiclePricingController Error Class: " + e.getClass().getName());
            System.err.println("❌ VehiclePricingController Error Stack Trace:");
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping
    public VehiclePricingDTO createPricing(@RequestBody VehiclePricingDTO dto) {
        return vehiclePricingService.createPricing(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehiclePricingDTO> updatePricing(@PathVariable Integer id,
                                                           @RequestBody VehiclePricingDTO dto) {
        VehiclePricingDTO updated = vehiclePricingService.updatePricing(id, dto);
        return (updated != null) ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePricing(@PathVariable Integer id) {
        vehiclePricingService.deletePricing(id);
        return ResponseEntity.noContent().build();
    }
}
