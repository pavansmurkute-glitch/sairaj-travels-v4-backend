package com.sairajtravels.site.controller;

import com.sairajtravels.site.dto.VehicleDTO;
import com.sairajtravels.site.dto.VehicleTypeDTO;
import com.sairajtravels.site.service.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public List<VehicleDTO> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    @GetMapping("/types")
    public List<VehicleTypeDTO> getVehicleTypes() {
        return vehicleService.getVehicleTypes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDTO> getVehicleById(@PathVariable Integer id) {
        VehicleDTO vehicle = vehicleService.getVehicleById(id);
        return (vehicle != null) ? ResponseEntity.ok(vehicle) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public VehicleDTO createVehicle(@RequestBody VehicleDTO dto) {
        return vehicleService.createVehicle(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleDTO> updateVehicle(@PathVariable Integer id, @RequestBody VehicleDTO dto) {
        VehicleDTO updated = vehicleService.updateVehicle(id, dto);
        return (updated != null) ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Integer id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }
}
