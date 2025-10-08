package com.sairajtravels.site.controller;

import com.sairajtravels.site.dto.VehicleBookingDTO;
import com.sairajtravels.site.service.VehicleBookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicle-bookings")
public class VehicleBookingController {

    private final VehicleBookingService vehicleBookingService;

    public VehicleBookingController(VehicleBookingService vehicleBookingService) {
        this.vehicleBookingService = vehicleBookingService;
    }

    @GetMapping
    public List<VehicleBookingDTO> getAllBookings() {
        return vehicleBookingService.getAllBookings();
    }

    @GetMapping("/vehicle/{vehicleId}")
    public List<VehicleBookingDTO> getBookingsByVehicle(@PathVariable Integer vehicleId) {
        return vehicleBookingService.getBookingsByVehicle(vehicleId);
    }

    @PostMapping
    public VehicleBookingDTO createBooking(@RequestBody VehicleBookingDTO dto) {
        return vehicleBookingService.createBooking(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleBookingDTO> updateBooking(@PathVariable Integer id,
                                                           @RequestBody VehicleBookingDTO dto) {
        System.out.println("=== VEHICLE BOOKING CONTROLLER ===");
        System.out.println("Booking ID: " + id);
        System.out.println("DTO Status: " + dto.getStatus());
        System.out.println("DTO Customer Email: " + dto.getCustomerEmail());
        
        VehicleBookingDTO updated = vehicleBookingService.updateBooking(id, dto);
        return (updated != null) ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Integer id) {
        vehicleBookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
}
