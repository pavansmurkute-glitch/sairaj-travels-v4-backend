package com.sairajtravels.site.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "VehicleTerms")
public class VehicleTerm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TermId")
    private Integer termId;

    // Foreign Key reference to Vehicle
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VehicleId")
    private Vehicle vehicle;

    @Column(name = "TermText", columnDefinition = "TEXT")
    private String termText;

    // ✅ Constructors
    public VehicleTerm() {}

    public VehicleTerm(Vehicle vehicle, String termText) {
        this.vehicle = vehicle;
        this.termText = termText;
    }

    // ✅ Getters & Setters
    public Integer getTermId() {
        return termId;
    }

    public void setTermId(Integer termId) {
        this.termId = termId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getTermText() {
        return termText;
    }

    public void setTermText(String termText) {
        this.termText = termText;
    }
}
