package com.sairajtravels.site.controller;

import com.sairajtravels.site.entity.AdminRole;
import com.sairajtravels.site.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/roles")
@CrossOrigin(origins = "*")
public class RoleManagementController {
    
    @Autowired
    private UserManagementService userService;
    
    // Get all roles
    @GetMapping
    public ResponseEntity<List<AdminRole>> getAllRoles() {
        try {
            List<AdminRole> roles = userService.getAllRoles();
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    // Get active roles only
    @GetMapping("/active")
    public ResponseEntity<List<AdminRole>> getActiveRoles() {
        try {
            List<AdminRole> roles = userService.getActiveRoles();
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    // Get role by ID
    @GetMapping("/{id}")
    public ResponseEntity<AdminRole> getRoleById(@PathVariable Long id) {
        try {
            Optional<AdminRole> role = userService.getRoleById(id);
            return role.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    // Get role permissions structure (for UI)
    @GetMapping("/permissions-structure")
    public ResponseEntity<Map<String, Object>> getPermissionsStructure() {
        try {
            Map<String, Object> structure = Map.of(
                "categories", Map.of(
                    "User Management", List.of(
                        Map.of("key", "users", "name", "Manage Users", "description", "Create, edit, delete users"),
                        Map.of("key", "roles", "name", "Manage Roles", "description", "Create, edit role permissions")
                    ),
                    "Business Operations", List.of(
                        Map.of("key", "bookings", "name", "Bookings", "description", "View and manage bookings"),
                        Map.of("key", "enquiries", "name", "Enquiries", "description", "View and manage enquiries"),
                        Map.of("key", "vehicles", "name", "Vehicles", "description", "Manage vehicle fleet"),
                        Map.of("key", "drivers", "name", "Drivers", "description", "Manage driver information"),
                        Map.of("key", "packages", "name", "Packages", "description", "Manage tour packages")
                    ),
                    "Content Management", List.of(
                        Map.of("key", "gallery", "name", "Gallery", "description", "Manage photo gallery"),
                        Map.of("key", "testimonials", "name", "Testimonials", "description", "Manage customer testimonials"),
                        Map.of("key", "contact", "name", "Contact", "description", "View contact messages"),
                        Map.of("key", "file_manager", "name", "File Manager", "description", "Upload and manage files")
                    ),
                    "Analytics", List.of(
                        Map.of("key", "reports", "name", "Reports", "description", "View business reports and analytics")
                    )
                )
            );
            return ResponseEntity.ok(structure);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    // Update role permissions
    @PutMapping("/{roleId}/permissions")
    public ResponseEntity<Map<String, Object>> updateRolePermissions(
            @PathVariable Long roleId,
            @RequestBody Map<String, Object> request) {
        try {
            AdminRole role = userService.getRoleById(roleId).orElse(null);
            if (role == null) {
                return ResponseEntity.notFound().build();
            }

            // Update permissions
            Map<String, Object> permissions = (Map<String, Object>) request.get("permissions");
            if (permissions != null) {
                role.setPermissions(permissions);
                userService.updateRole(role);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Permissions updated successfully");
            response.put("role", role);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to update permissions: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
