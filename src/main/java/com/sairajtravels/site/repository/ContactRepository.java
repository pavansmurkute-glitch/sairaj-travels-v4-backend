package com.sairajtravels.site.repository;

import com.sairajtravels.site.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Integer> {}
