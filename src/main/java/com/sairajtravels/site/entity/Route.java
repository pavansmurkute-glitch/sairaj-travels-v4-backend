package com.sairajtravels.site.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "routes")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    private Integer routeId;

    @Column(name = "route_source", nullable = false)
    private String source;

    @Column(name = "route_destination", nullable = false)
    private String destination;

    @Column(name = "route_distance_km")
    private Integer distanceKm;

    @Column(name = "route_duration_hrs")
    private Double durationHrs;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters & Setters
    public Integer getRouteId() { return routeId; }
    public void setRouteId(Integer routeId) { this.routeId = routeId; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public Integer getDistanceKm() { return distanceKm; }
    public void setDistanceKm(Integer distanceKm) { this.distanceKm = distanceKm; }

    public Double getDurationHrs() { return durationHrs; }
    public void setDurationHrs(Double durationHrs) { this.durationHrs = durationHrs; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
