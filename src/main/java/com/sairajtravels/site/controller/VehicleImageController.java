package com.sairajtravels.site.controller;

import com.sairajtravels.site.dto.VehicleImageDTO;
import com.sairajtravels.site.service.VehicleImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicle-images")
public class VehicleImageController {

    private final VehicleImageService vehicleImageService;

    public VehicleImageController(VehicleImageService vehicleImageService) {
        this.vehicleImageService = vehicleImageService;
    }

    @GetMapping
    public List<VehicleImageDTO> getAllImages() {
        return vehicleImageService.getAllImages();
    }

    @GetMapping("/vehicle/{vehicleId}")
    public List<VehicleImageDTO> getImagesByVehicle(@PathVariable Integer vehicleId) {
        return vehicleImageService.getImagesByVehicle(vehicleId);
    }

    @PostMapping
    public VehicleImageDTO createImage(@RequestBody VehicleImageDTO dto) {
        return vehicleImageService.createImage(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleImageDTO> updateImage(@PathVariable Integer id,
                                                     @RequestBody VehicleImageDTO dto) {
        VehicleImageDTO updated = vehicleImageService.updateImage(id, dto);
        return (updated != null) ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Integer id) {
        vehicleImageService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}
