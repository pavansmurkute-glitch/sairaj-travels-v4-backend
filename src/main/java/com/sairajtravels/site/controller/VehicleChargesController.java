package com.sairajtravels.site.controller;

import com.sairajtravels.site.dto.VehicleChargesDTO;
import com.sairajtravels.site.service.VehicleChargesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicle-charges")
public class VehicleChargesController {

    private final VehicleChargesService vehicleChargesService;

    public VehicleChargesController(VehicleChargesService vehicleChargesService) {
        this.vehicleChargesService = vehicleChargesService;
    }

    @GetMapping
    public List<VehicleChargesDTO> getAllCharges() {
        return vehicleChargesService.getAllCharges();
    }

    @GetMapping("/vehicle/{vehicleId}")
    public List<VehicleChargesDTO> getChargesByVehicle(@PathVariable Integer vehicleId) {
        System.out.println("🔍 VehicleChargesController: getChargesByVehicle called with vehicleId: " + vehicleId);
        try {
            List<VehicleChargesDTO> result = vehicleChargesService.getChargesByVehicle(vehicleId);
            System.out.println("✅ VehicleChargesController: Successfully retrieved " + result.size() + " charges for vehicle " + vehicleId);
            return result;
        } catch (Exception e) {
            System.err.println("❌ VehicleChargesController Error: " + e.getMessage());
            System.err.println("❌ VehicleChargesController Error Class: " + e.getClass().getName());
            System.err.println("❌ VehicleChargesController Error Stack Trace:");
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping
    public VehicleChargesDTO createCharges(@RequestBody VehicleChargesDTO dto) {
        return vehicleChargesService.createCharges(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleChargesDTO> updateCharges(@PathVariable Integer id,
                                                           @RequestBody VehicleChargesDTO dto) {
        VehicleChargesDTO updated = vehicleChargesService.updateCharges(id, dto);
        return (updated != null) ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCharges(@PathVariable Integer id) {
        vehicleChargesService.deleteCharges(id);
        return ResponseEntity.noContent().build();
    }
}
