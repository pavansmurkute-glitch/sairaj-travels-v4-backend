package com.sairajtravels.site.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

@Entity
@Table(name = "admin_roles")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AdminRole {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "role_name", unique = true, nullable = false, length = 50)
    private String roleName;
    
    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "permissions", columnDefinition = "JSON")
    private String permissions;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public AdminRole() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public AdminRole(String roleName, String displayName, String description) {
        this();
        this.roleName = roleName;
        this.displayName = displayName;
        this.description = description;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getRoleName() {
        return roleName;
    }
    
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getPermissions() {
        return permissions;
    }
    
    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }
    
    public void setPermissions(Map<String, Object> permissionsMap) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            this.permissions = mapper.writeValueAsString(permissionsMap);
        } catch (Exception e) {
            this.permissions = "{}";
        }
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Utility methods for permissions
    public Map<String, Boolean> getPermissionsMap() {
        if (permissions == null || permissions.isEmpty()) {
            return new HashMap<>();
        }
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(permissions);
            Map<String, Boolean> permMap = new HashMap<>();
            
            node.fieldNames().forEachRemaining(fieldName -> {
                permMap.put(fieldName, node.get(fieldName).asBoolean());
            });
            
            return permMap;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }
    
    public void setPermissionsMap(Map<String, Boolean> permissionsMap) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            this.permissions = mapper.writeValueAsString(permissionsMap);
        } catch (Exception e) {
            this.permissions = "{}";
        }
    }
    
    public boolean hasPermission(String permission) {
        Map<String, Boolean> permMap = getPermissionsMap();
        return permMap.getOrDefault(permission, false);
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
