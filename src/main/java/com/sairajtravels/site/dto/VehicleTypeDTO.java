package com.sairajtravels.site.dto;

public class VehicleTypeDTO {
    private String id;
    private String name;
    private Integer capacity;
    private String type;
    private Boolean isAC;

    // Constructors
    public VehicleTypeDTO() {}

    public VehicleTypeDTO(String id, String name, Integer capacity, String type, Boolean isAC) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.type = type;
        this.isAC = isAC;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsAC() {
        return isAC;
    }

    public void setIsAC(Boolean isAC) {
        this.isAC = isAC;
    }
}
