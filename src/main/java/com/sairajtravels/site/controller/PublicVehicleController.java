package com.sairajtravels.site.controller;

import com.sairajtravels.site.dto.VehicleDTO;
import com.sairajtravels.site.dto.VehicleTypeDTO;
import com.sairajtravels.site.service.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles/public")
@CrossOrigin(origins = "${app.cors.allowed-origins:*}")
public class PublicVehicleController {

    private final VehicleService vehicleService;

    public PublicVehicleController(VehicleService vehicleService) {
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
}
