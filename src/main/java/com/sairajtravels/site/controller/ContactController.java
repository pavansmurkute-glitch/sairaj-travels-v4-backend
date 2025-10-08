package com.sairajtravels.site.controller;

import com.sairajtravels.site.entity.Contact;
import com.sairajtravels.site.repository.ContactRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
@CrossOrigin(origins = "*")
public class ContactController {

    private final ContactRepository repo;

    public ContactController(ContactRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Contact> getAllContacts() {
        return repo.findAll();
    }

    @PostMapping
    public Contact createContact(@RequestBody Contact contact) {
        return repo.save(contact);
    }
}
