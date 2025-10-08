package com.sairajtravels.site.controller;

import com.sairajtravels.site.dto.DriverDTO;
import com.sairajtravels.site.service.DriverService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    // ✅ Get all drivers
    @GetMapping
    public ResponseEntity<List<DriverDTO>> getAllDrivers() {
        return ResponseEntity.ok(driverService.getAllDrivers());
    }

    // ✅ Get driver by ID
    @GetMapping("/{id}")
    public ResponseEntity<DriverDTO> getDriverById(@PathVariable Integer id) {
        DriverDTO dto = driverService.getDriverById(id);
        return (dto != null) ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    // ✅ Add new driver
    @PostMapping
    public ResponseEntity<DriverDTO> addDriver(@RequestBody DriverDTO driverDTO) {
        return ResponseEntity.ok(driverService.addDriver(driverDTO));
    }

    // ✅ Update driver
    @PutMapping("/{id}")
    public ResponseEntity<DriverDTO> updateDriver(
            @PathVariable Integer id,
            @RequestBody DriverDTO driverDTO
    ) {
        DriverDTO updated = driverService.updateDriver(id, driverDTO);
        return (updated != null) ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // ✅ Delete driver
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Integer id) {
        driverService.deleteDriver(id);
        return ResponseEntity.noContent().build();
    }
}
