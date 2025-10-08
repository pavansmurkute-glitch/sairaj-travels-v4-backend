package com.sairajtravels.site.service;

import com.sairajtravels.site.entity.ContactInfo;
import com.sairajtravels.site.repository.ContactInfoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContactInfoService {

    private final ContactInfoRepository repository;

    public ContactInfoService(ContactInfoRepository repository) {
        this.repository = repository;
    }

    // Fetch single record (we assume only 1 record exists in the table)
    public Optional<ContactInfo> getContactInfo() {
        return repository.findById(1L);
    }

    // Debug method to check record count
    public long getContactInfoCount() {
        return repository.count();
    }

    // Create contact info
    public ContactInfo createContactInfo(ContactInfo info) {
        return repository.save(info);
    }

    // Update contact info (optional, for future admin panel)
    public ContactInfo updateContactInfo(ContactInfo info) {
        return repository.save(info);
    }
}
