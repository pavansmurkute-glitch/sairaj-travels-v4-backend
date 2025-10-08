package com.sairajtravels.site.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VehicleId")
    private Integer vehicleId;

    @Column(name = "Name", nullable = false, length = 100)
    private String name;

    @Column(name = "Type", length = 50)
    private String type;

    @Column(name = "Capacity")
    private Integer capacity;

    @Column(name = "IsAC")
    private Boolean isAC;

    @Column(name = "Description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    // ✅ New column for Fleet main image
    @Column(name = "MainImageUrl", length = 500)
    private String mainImageUrl;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    // ✅ One vehicle can have many images
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VehicleImage> images = new ArrayList<>();

    // ✅ One vehicle can have multiple pricing plans
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VehiclePricing> pricingList = new ArrayList<>();

    // ✅ One vehicle can have multiple charges
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VehicleCharges> chargesList = new ArrayList<>();

    // ✅ One vehicle can have multiple terms
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VehicleTerm> terms = new ArrayList<>();

    // ✅ One vehicle can have multiple bookings
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VehicleBooking> bookings = new ArrayList<>();

    // ✅ Constructors
    public Vehicle() {}

    public Vehicle(String name, String type, Integer capacity, Boolean isAC,
                   String description, String mainImageUrl, LocalDateTime createdAt) {
        this.name = name;
        this.type = type;
        this.capacity = capacity;
        this.isAC = isAC;
        this.description = description;
        this.mainImageUrl = mainImageUrl;
        this.createdAt = createdAt;
    }

    // ✅ Getters & Setters
    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Boolean getIsAC() {
        return isAC;
    }

    public void setIsAC(Boolean isAC) {
        this.isAC = isAC;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<VehicleImage> getImages() {
        return images;
    }

    public void setImages(List<VehicleImage> images) {
        this.images = images;
    }

    public List<VehiclePricing> getPricingList() {
        return pricingList;
    }

    public void setPricingList(List<VehiclePricing> pricingList) {
        this.pricingList = pricingList;
    }

    public List<VehicleCharges> getChargesList() {
        return chargesList;
    }

    public void setChargesList(List<VehicleCharges> chargesList) {
        this.chargesList = chargesList;
    }

    public List<VehicleTerm> getTerms() {
        return terms;
    }

    public void setTerms(List<VehicleTerm> terms) {
        this.terms = terms;
    }

    public List<VehicleBooking> getBookings() {
        return bookings;
    }

    public void setBookings(List<VehicleBooking> bookings) {
        this.bookings = bookings;
    }

    // ✅ Helper methods for Images
    public void addImage(VehicleImage image) {
        images.add(image);
        image.setVehicle(this);
    }

    public void removeImage(VehicleImage image) {
        images.remove(image);
        image.setVehicle(null);
    }

    // ✅ Helper methods for Pricing
    public void addPricing(VehiclePricing pricing) {
        pricingList.add(pricing);
        pricing.setVehicle(this);
    }

    public void removePricing(VehiclePricing pricing) {
        pricingList.remove(pricing);
        pricing.setVehicle(null);
    }

    // ✅ Helper methods for Charges
    public void addCharge(VehicleCharges charge) {
        chargesList.add(charge);
        charge.setVehicle(this);
    }

    public void removeCharge(VehicleCharges charge) {
        chargesList.remove(charge);
        charge.setVehicle(null);
    }

    // ✅ Helper methods for Terms
    public void addTerm(VehicleTerm term) {
        terms.add(term);
        term.setVehicle(this);
    }

    public void removeTerm(VehicleTerm term) {
        terms.remove(term);
        term.setVehicle(null);
    }

    // ✅ Helper methods for Bookings
    public void addBooking(VehicleBooking booking) {
        bookings.add(booking);
        booking.setVehicle(this);
    }

    public void removeBooking(VehicleBooking booking) {
        bookings.remove(booking);
        booking.setVehicle(null);
    }
}
