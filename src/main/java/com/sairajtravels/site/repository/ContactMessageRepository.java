package com.sairajtravels.site.repository;

import com.sairajtravels.site.entity.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Integer> {
    // standard CRUD methods available
}
