package com.sairajtravels.site.repository;

import com.sairajtravels.site.entity.VehicleImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleImageRepository extends JpaRepository<VehicleImage, Integer> {
    List<VehicleImage> findByVehicle_VehicleId(Integer vehicleId);
}
