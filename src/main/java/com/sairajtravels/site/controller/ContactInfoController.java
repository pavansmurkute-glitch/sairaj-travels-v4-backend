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
                ContactInfo defaultContact = new ContactInfo();
                defaultContact.setId(1L);
                defaultContact.setPhoneOffice("+91 98507 48273");
                defaultContact.setPhoneMobile("+91 98507 48273");
                defaultContact.setPhoneWhatsapp("+91 98507 48273");
                defaultContact.setEmailPrimary("info@sairajtravels.com");
                defaultContact.setEmailBookings("bookings@sairajtravels.com");
                defaultContact.setEmailSupport("support@sairajtravels.com");
                defaultContact.setAddressLine1("Sairaj Travels Office, Pune");
                defaultContact.setAddressCity("Pune");
                defaultContact.setAddressState("Maharashtra");
                defaultContact.setAddressPincode("411001");
                defaultContact.setBusinessHoursWeekdays("24/7 Available");
                defaultContact.setBusinessHoursSunday("24/7 Available");
                defaultContact.setSocialFacebook("https://facebook.com/sairajtravels");
                defaultContact.setSocialInstagram("https://instagram.com/sairajtravels");
                defaultContact.setSocialLinkedin("https://linkedin.com/company/sairajtravels");
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
        info.setId(id);
        return service.updateContactInfo(info);
    }
}
