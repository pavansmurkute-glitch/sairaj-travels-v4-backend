package com.sairajtravels.site.controller;

import com.sairajtravels.site.entity.AdminUser;
import com.sairajtravels.site.entity.AdminRole;
import com.sairajtravels.site.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/users")
@CrossOrigin(origins = "*")
public class UserManagementController {
    
    @Autowired
    private UserManagementService userService;
    
    // Get all users
    @GetMapping
    public ResponseEntity<List<AdminUser>> getAllUsers() {
        try {
            List<AdminUser> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    // Get active users only
    @GetMapping("/active")
    public ResponseEntity<List<AdminUser>> getActiveUsers() {
        try {
            List<AdminUser> users = userService.getActiveUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<AdminUser> getUserById(@PathVariable Long id) {
        try {
            Optional<AdminUser> user = userService.getUserById(id);
            return user.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    // Search users
    @GetMapping("/search")
    public ResponseEntity<List<AdminUser>> searchUsers(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) Boolean isActive) {
        try {
            List<AdminUser> users = userService.searchUsers(searchTerm, isActive);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    // Create new user
    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody AdminUser user, 
                                                          @RequestParam(required = false) Long createdById) {
        try {
            AdminUser createdUser = userService.createUser(user, createdById);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "User created successfully",
                "user", createdUser
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
    
    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Long id, @RequestBody AdminUser user) {
        try {
            user.setId(id);
            AdminUser updatedUser = userService.updateUser(user);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "User updated successfully",
                "user", updatedUser
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
    
    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "User deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
    
    // Activate user
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Map<String, Object>> activateUser(@PathVariable Long id) {
        try {
            userService.activateUser(id);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "User activated successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
    
    // Deactivate user
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Map<String, Object>> deactivateUser(@PathVariable Long id) {
        try {
            userService.deactivateUser(id);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "User deactivated successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
    
    // Change password
    @PostMapping("/{id}/change-password")
    public ResponseEntity<Map<String, Object>> changePassword(@PathVariable Long id, 
                                                             @RequestBody Map<String, String> passwords) {
        try {
            String currentPassword = passwords.get("currentPassword");
            String newPassword = passwords.get("newPassword");
            
            if (currentPassword == null || newPassword == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Current password and new password are required"
                ));
            }
            
            boolean success = userService.changePassword(id, currentPassword, newPassword);
            if (success) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Password changed successfully"
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Current password is incorrect"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
    
    // Reset password (admin action)
    @PostMapping("/{id}/reset-password")
    public ResponseEntity<Map<String, Object>> resetPassword(@PathVariable Long id, 
                                                            @RequestBody Map<String, String> request) {
        try {
            String newPassword = request.get("newPassword");
            
            if (newPassword == null || newPassword.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "New password is required"
                ));
            }
            
            boolean success = userService.resetPassword(id, newPassword);
            if (success) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Password reset successfully. User must change password on next login."
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "User not found"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
    
    // Get user statistics
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getUserStats() {
        try {
            long totalUsers = userService.getUserCount();
            long activeUsers = userService.getActiveUserCount();
            
            return ResponseEntity.ok(Map.of(
                "totalUsers", totalUsers,
                "activeUsers", activeUsers,
                "inactiveUsers", totalUsers - activeUsers
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Failed to get user statistics"
            ));
        }
    }
}
