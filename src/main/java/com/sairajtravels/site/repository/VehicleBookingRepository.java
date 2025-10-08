package com.sairajtravels.site.repository;

import com.sairajtravels.site.entity.VehicleBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleBookingRepository extends JpaRepository<VehicleBooking, Integer> {
    List<VehicleBooking> findByVehicle_VehicleId(Integer vehicleId);
}
