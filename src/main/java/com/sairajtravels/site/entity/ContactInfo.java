package com.sairajtravels.site.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contact_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ContactInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ✅ works with SQL Server IDENTITY
    private Long id;

    @Column(name = "phone_office")
    private String phoneOffice;

    @Column(name = "phone_mobile")
    private String phoneMobile;

    @Column(name = "phone_whatsapp")
    private String phoneWhatsapp;

    @Column(name = "email_primary")
    private String emailPrimary;

    @Column(name = "email_bookings")
    private String emailBookings;

    @Column(name = "email_support")
    private String emailSupport;

    @Column(name = "address_line1")
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    @Column(name = "address_city")
    private String addressCity;

    @Column(name = "address_state")
    private String addressState;

    @Column(name = "address_pincode")
    private String addressPincode;

    @Column(name = "business_hours_weekdays")
    private String businessHoursWeekdays;

    @Column(name = "business_hours_sunday")
    private String businessHoursSunday;

    @Column(name = "social_facebook")
    private String socialFacebook;

    @Column(name = "social_instagram")
    private String socialInstagram;

    @Column(name = "social_linkedin")
    private String socialLinkedin;

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;
}
