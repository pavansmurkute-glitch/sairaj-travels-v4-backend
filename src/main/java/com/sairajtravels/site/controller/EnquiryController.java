package com.sairajtravels.site.controller;

import com.sairajtravels.site.dto.EnquiryDTO;
import com.sairajtravels.site.service.EnquiryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enquiries")
public class EnquiryController {

    private final EnquiryService enquiryService;

    public EnquiryController(EnquiryService enquiryService) {
        this.enquiryService = enquiryService;
    }

    @PostMapping
    public ResponseEntity<EnquiryDTO> createEnquiry(@RequestBody EnquiryDTO enquiry) {
        try {
            EnquiryDTO saved = enquiryService.createEnquiry(enquiry);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<EnquiryDTO>> getAllEnquiries() {
        try {
            List<EnquiryDTO> enquiries = enquiryService.getAllEnquiries();
            return ResponseEntity.ok(enquiries);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnquiryDTO> getEnquiryById(@PathVariable Integer id) {
        try {
            EnquiryDTO enquiry = enquiryService.getEnquiryById(id);
            if (enquiry != null) {
                return ResponseEntity.ok(enquiry);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<EnquiryDTO>> getEnquiriesByStatus(@PathVariable String status) {
        try {
            List<EnquiryDTO> enquiries = enquiryService.getEnquiriesByStatus(status);
            return ResponseEntity.ok(enquiries);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/service/{service}")
    public ResponseEntity<List<EnquiryDTO>> getEnquiriesByService(@PathVariable String service) {
        try {
            List<EnquiryDTO> enquiries = enquiryService.getEnquiriesByService(service);
            return ResponseEntity.ok(enquiries);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnquiryDTO> updateEnquiry(@PathVariable Integer id, @RequestBody EnquiryDTO enquiry) {
        try {
            EnquiryDTO updated = enquiryService.updateEnquiry(id, enquiry);
            if (updated != null) {
                return ResponseEntity.ok(updated);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnquiry(@PathVariable Integer id) {
        try {
            enquiryService.deleteEnquiry(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
