package com.sairajtravels.site.controller;

import com.sairajtravels.site.dto.VehicleTermDTO;
import com.sairajtravels.site.service.VehicleTermService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicle-terms")
public class VehicleTermController {

    private final VehicleTermService vehicleTermService;

    public VehicleTermController(VehicleTermService vehicleTermService) {
        this.vehicleTermService = vehicleTermService;
    }

    @GetMapping
    public List<VehicleTermDTO> getAllTerms() {
        return vehicleTermService.getAllTerms();
    }

    @GetMapping("/vehicle/{vehicleId}")
    public List<VehicleTermDTO> getTermsByVehicle(@PathVariable Integer vehicleId) {
        return vehicleTermService.getTermsByVehicle(vehicleId);
    }

    @PostMapping
    public VehicleTermDTO createTerm(@RequestBody VehicleTermDTO dto) {
        return vehicleTermService.createTerm(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleTermDTO> updateTerm(@PathVariable Integer id,
                                                     @RequestBody VehicleTermDTO dto) {
        VehicleTermDTO updated = vehicleTermService.updateTerm(id, dto);
        return (updated != null) ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTerm(@PathVariable Integer id) {
        vehicleTermService.deleteTerm(id);
        return ResponseEntity.noContent().build();
    }
}
