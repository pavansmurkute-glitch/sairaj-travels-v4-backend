package com.sairajtravels.site.service;

import com.sairajtravels.site.dto.TestimonialDTO;
import com.sairajtravels.site.entity.Testimonial;
import com.sairajtravels.site.repository.TestimonialRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TestimonialService {
    
    private final TestimonialRepository repository;
    
    public TestimonialService(TestimonialRepository repository) {
        this.repository = repository;
    }
    
    // Get all active testimonials for public display
    public List<TestimonialDTO> getAllActiveTestimonials() {
        List<Testimonial> testimonials = repository.findAllActiveOrdered();
        return testimonials.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Get all testimonials for admin panel
    public List<TestimonialDTO> getAllTestimonials() {
        List<Testimonial> testimonials = repository.findAllOrdered();
        return testimonials.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Get testimonial by ID
    public Optional<TestimonialDTO> getTestimonialById(Long id) {
        return repository.findById(id)
                .map(this::convertToDTO);
    }
    
    // Create new testimonial
    public TestimonialDTO createTestimonial(TestimonialDTO dto) {
        Testimonial testimonial = convertToEntity(dto);
        testimonial = repository.save(testimonial);
        return convertToDTO(testimonial);
    }
    
    // Update existing testimonial
    public Optional<TestimonialDTO> updateTestimonial(Long id, TestimonialDTO dto) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setCustomerName(dto.getCustomerName());
                    existing.setCustomerType(dto.getCustomerType());
                    existing.setTestimonialText(dto.getTestimonialText());
                    existing.setRating(dto.getRating());
                    existing.setAvatarLetter(dto.getAvatarLetter());
                    existing.setIsActive(dto.getIsActive());
                    existing.setSortOrder(dto.getSortOrder());
                    existing = repository.save(existing);
                    return convertToDTO(existing);
                });
    }
    
    // Delete testimonial
    public boolean deleteTestimonial(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Toggle testimonial active status
    public Optional<TestimonialDTO> toggleActiveStatus(Long id) {
        return repository.findById(id)
                .map(testimonial -> {
                    testimonial.setIsActive(!testimonial.getIsActive());
                    testimonial = repository.save(testimonial);
                    return convertToDTO(testimonial);
                });
    }
    
    // Convert Entity to DTO
    private TestimonialDTO convertToDTO(Testimonial testimonial) {
        TestimonialDTO dto = new TestimonialDTO();
        dto.setId(testimonial.getId());
        dto.setCustomerName(testimonial.getCustomerName());
        dto.setCustomerType(testimonial.getCustomerType());
        dto.setTestimonialText(testimonial.getTestimonialText());
        dto.setRating(testimonial.getRating());
        dto.setAvatarLetter(testimonial.getAvatarLetter());
        dto.setIsActive(testimonial.getIsActive());
        dto.setSortOrder(testimonial.getSortOrder());
        dto.setCreatedAt(testimonial.getCreatedAt());
        dto.setUpdatedAt(testimonial.getUpdatedAt());
        return dto;
    }
    
    // Convert DTO to Entity
    private Testimonial convertToEntity(TestimonialDTO dto) {
        Testimonial testimonial = new Testimonial();
        testimonial.setId(dto.getId());
        testimonial.setCustomerName(dto.getCustomerName());
        testimonial.setCustomerType(dto.getCustomerType());
        testimonial.setTestimonialText(dto.getTestimonialText());
        testimonial.setRating(dto.getRating());
        testimonial.setAvatarLetter(dto.getAvatarLetter());
        testimonial.setIsActive(dto.getIsActive());
        testimonial.setSortOrder(dto.getSortOrder());
        return testimonial;
    }
}
