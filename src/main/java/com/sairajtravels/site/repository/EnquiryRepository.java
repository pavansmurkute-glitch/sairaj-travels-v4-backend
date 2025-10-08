package com.sairajtravels.site.repository;

import com.sairajtravels.site.entity.Enquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnquiryRepository extends JpaRepository<Enquiry, Integer> {
    
    List<Enquiry> findByStatus(String status);
    
    List<Enquiry> findByService(String service);
    
    List<Enquiry> findByEmail(String email);
    
    List<Enquiry> findByPhone(String phone);
}
