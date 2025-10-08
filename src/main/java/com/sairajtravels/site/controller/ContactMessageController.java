package com.sairajtravels.site.controller;

import com.sairajtravels.site.entity.ContactMessage;
import com.sairajtravels.site.service.ContactMessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact-messages")
@CrossOrigin(origins = "*")
public class ContactMessageController {

    private final ContactMessageService service;

    public ContactMessageController(ContactMessageService service) {
        this.service = service;
    }

    // Create
    @PostMapping
    public ResponseEntity<ContactMessage> saveMessage(@RequestBody ContactMessage message) {
        ContactMessage saved = service.saveMessage(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Read all (admin)
    @GetMapping
    public ResponseEntity<List<ContactMessage>> getAllMessages() {
        List<ContactMessage> list = service.getAllMessages();
        return ResponseEntity.ok(list);
    }

    // Read one
    @GetMapping("/{id}")
    public ResponseEntity<ContactMessage> getMessageById(@PathVariable Integer id) {
        return service.getMessageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Integer id) {
        service.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}
