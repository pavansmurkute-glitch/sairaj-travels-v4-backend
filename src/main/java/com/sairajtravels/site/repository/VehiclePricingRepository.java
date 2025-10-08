package com.sairajtravels.site.repository;

import com.sairajtravels.site.entity.VehiclePricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehiclePricingRepository extends JpaRepository<VehiclePricing, Integer> {
    List<VehiclePricing> findByVehicle_VehicleId(Integer vehicleId);
}
