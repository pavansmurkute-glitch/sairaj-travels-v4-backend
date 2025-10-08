package com.sairajtravels.site.repository;

import com.sairajtravels.site.entity.VehicleTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleTermRepository extends JpaRepository<VehicleTerm, Integer> {
    List<VehicleTerm> findByVehicle_VehicleId(Integer vehicleId);
}
