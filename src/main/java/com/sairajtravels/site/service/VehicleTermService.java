package com.sairajtravels.site.service;

import com.sairajtravels.site.dto.VehicleTermDTO;
import com.sairajtravels.site.entity.Vehicle;
import com.sairajtravels.site.entity.VehicleTerm;
import com.sairajtravels.site.repository.VehicleRepository;
import com.sairajtravels.site.repository.VehicleTermRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleTermService {

    private final VehicleTermRepository vehicleTermRepository;
    private final VehicleRepository vehicleRepository;

    public VehicleTermService(VehicleTermRepository vehicleTermRepository, VehicleRepository vehicleRepository) {
        this.vehicleTermRepository = vehicleTermRepository;
        this.vehicleRepository = vehicleRepository;
    }

    private VehicleTermDTO convertToDTO(VehicleTerm term) {
        return new VehicleTermDTO(
                term.getTermId(),
                term.getVehicle() != null ? term.getVehicle().getVehicleId() : null,
                term.getTermText()
        );
    }

    private VehicleTerm convertToEntity(VehicleTermDTO dto) {
        VehicleTerm term = new VehicleTerm();
        term.setTermId(dto.getTermId());
        term.setTermText(dto.getTermText());

        if (dto.getVehicleId() != null) {
            Optional<Vehicle> vehicle = vehicleRepository.findById(dto.getVehicleId());
            vehicle.ifPresent(term::setVehicle);
        }
        return term;
    }

    public List<VehicleTermDTO> getAllTerms() {
        return vehicleTermRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<VehicleTermDTO> getTermsByVehicle(Integer vehicleId) {
        return vehicleTermRepository.findByVehicle_VehicleId(vehicleId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public VehicleTermDTO createTerm(VehicleTermDTO dto) {
        VehicleTerm saved = vehicleTermRepository.save(convertToEntity(dto));
        return convertToDTO(saved);
    }

    public VehicleTermDTO updateTerm(Integer id, VehicleTermDTO dto) {
        if (vehicleTermRepository.existsById(id)) {
            dto.setTermId(id);
            VehicleTerm updated = vehicleTermRepository.save(convertToEntity(dto));
            return convertToDTO(updated);
        }
        return null;
    }

    public void deleteTerm(Integer id) {
        vehicleTermRepository.deleteById(id);
    }
}
