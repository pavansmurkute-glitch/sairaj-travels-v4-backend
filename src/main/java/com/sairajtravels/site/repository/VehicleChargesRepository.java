package com.sairajtravels.site.repository;

import com.sairajtravels.site.entity.VehicleCharges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleChargesRepository extends JpaRepository<VehicleCharges, Integer> {
    List<VehicleCharges> findByVehicle_VehicleId(Integer vehicleId);
}
