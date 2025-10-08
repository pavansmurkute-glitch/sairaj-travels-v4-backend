package com.sairajtravels.site.repository;

import com.sairajtravels.site.entity.Testimonial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestimonialRepository extends JpaRepository<Testimonial, Long> {
    
    // Find all active testimonials ordered by sort order
    @Query("SELECT t FROM Testimonial t WHERE t.isActive = true ORDER BY t.sortOrder ASC, t.createdAt ASC")
    List<Testimonial> findAllActiveOrdered();
    
    // Find all testimonials ordered by sort order (for admin)
    @Query("SELECT t FROM Testimonial t ORDER BY t.sortOrder ASC, t.createdAt ASC")
    List<Testimonial> findAllOrdered();
    
    // Find testimonials by customer type
    List<Testimonial> findByCustomerTypeAndIsActiveTrueOrderBySortOrderAsc(String customerType);
    
    // Find testimonials by rating
    List<Testimonial> findByRatingAndIsActiveTrueOrderBySortOrderAsc(Integer rating);
    
    // Count active testimonials
    long countByIsActiveTrue();
}
