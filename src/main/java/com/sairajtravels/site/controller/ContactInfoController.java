package com.sairajtravels.site.controller;

import com.sairajtravels.site.entity.ContactInfo;
import com.sairajtravels.site.service.ContactInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "*")
public class ContactInfoController {

    private final ContactInfoService service;

    public ContactInfoController(ContactInfoService service) {
        this.service = service;
    }

    // ✅ Fetch contact info
    @GetMapping
    public ResponseEntity<ContactInfo> getContactInfo() {
        try {
            Optional<ContactInfo> contactInfoOpt = service.getContactInfo();
            
            if (contactInfoOpt.isPresent()) {
                return ResponseEntity.ok(contactInfoOpt.get());
            } else {
                // Return default contact info if none exists in database
                ContactInfo defaultContact = new ContactInfo(
                    1L, // id
                    "+91 98507 48273", // phoneOffice
                    "+91 98507 48273", // phoneMobile
                    "+91 98507 48273", // phoneWhatsapp
                    "info@sairajtravels.com", // emailPrimary
                    "bookings@sairajtravels.com", // emailBookings
                    "support@sairajtravels.com", // emailSupport
                    "Sairaj Travels Office, Pune", // addressLine1
                    null, // addressLine2
                    "Pune", // addressCity
                    "Maharashtra", // addressState
                    "411001", // addressPincode
                    "24/7 Available", // businessHoursWeekdays
                    "24/7 Available", // businessHoursSunday
                    "https://facebook.com/sairajtravels", // socialFacebook
                    "https://instagram.com/sairajtravels", // socialInstagram
                    "https://linkedin.com/company/sairajtravels", // socialLinkedin
                    null, // createdAt
                    null  // updatedAt
                );
                return ResponseEntity.ok(defaultContact);
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }


    // ✅ Create contact info
    @PostMapping
    public ContactInfo createContactInfo(@RequestBody ContactInfo info) {
        return service.createContactInfo(info);
    }

    // ✅ Update contact info (optional, for admin panel)
    @PutMapping("/{id}")
    public ContactInfo updateContactInfo(@PathVariable Long id, @RequestBody ContactInfo info) {
        // Create a new ContactInfo with the provided id
        ContactInfo updatedInfo = new ContactInfo(
            id, // id
            info.getPhoneOffice(),
            info.getPhoneMobile(),
            info.getPhoneWhatsapp(),
            info.getEmailPrimary(),
            info.getEmailBookings(),
            info.getEmailSupport(),
            info.getAddressLine1(),
            info.getAddressLine2(),
            info.getAddressCity(),
            info.getAddressState(),
            info.getAddressPincode(),
            info.getBusinessHoursWeekdays(),
            info.getBusinessHoursSunday(),
            info.getSocialFacebook(),
            info.getSocialInstagram(),
            info.getSocialLinkedin(),
            info.getCreatedAt(),
            info.getUpdatedAt()
        );
        return service.updateContactInfo(updatedInfo);
    }
}
