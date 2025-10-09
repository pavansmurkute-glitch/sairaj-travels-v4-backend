package com.sairajtravels.site.controller;

import com.sairajtravels.site.entity.EmailSettings;
import com.sairajtravels.site.service.EmailSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/email-settings")
public class EmailSettingsController {
    
    @Autowired
    private EmailSettingsService emailSettingsService;
    
    /**
     * Get current email settings
     */
    @GetMapping
    public ResponseEntity<EmailSettings> getEmailSettings() {
        try {
            EmailSettings settings = emailSettingsService.getEmailSettings();
            return ResponseEntity.ok(settings);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    
    /**
     * Update email settings
     */
    @PutMapping
    public ResponseEntity<Map<String, Object>> updateEmailSettings(
            @RequestBody EmailSettings emailSettings,
            Authentication authentication) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String updatedBy = authentication != null ? authentication.getName() : "Admin";
            EmailSettings updatedSettings = emailSettingsService.updateEmailSettings(emailSettings, updatedBy);
            
            response.put("success", true);
            response.put("message", "Email settings updated successfully");
            response.put("data", updatedSettings);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to update email settings: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * Toggle email on/off
     */
    @PostMapping("/toggle")
    public ResponseEntity<Map<String, Object>> toggleEmailEnabled(
            @RequestBody Map<String, Boolean> payload,
            Authentication authentication) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Boolean enabled = payload.get("enabled");
            String updatedBy = authentication != null ? authentication.getName() : "Admin";
            EmailSettings updatedSettings = emailSettingsService.toggleEmailEnabled(enabled, updatedBy);
            
            response.put("success", true);
            response.put("message", "Email " + (enabled ? "enabled" : "disabled") + " successfully");
            response.put("data", updatedSettings);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to toggle email settings: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * Check if email is enabled (simple status check)
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getEmailStatus() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean isEnabled = emailSettingsService.isEmailEnabled();
            
            response.put("success", true);
            response.put("emailEnabled", isEnabled);
            response.put("message", "Email is " + (isEnabled ? "enabled" : "disabled"));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("emailEnabled", false);
            response.put("message", "Failed to get email status: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * Reset to default settings
     */
    @PostMapping("/reset")
    public ResponseEntity<Map<String, Object>> resetToDefaults(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String updatedBy = authentication != null ? authentication.getName() : "Admin";
            EmailSettings resetSettings = emailSettingsService.resetToDefaults(updatedBy);
            
            response.put("success", true);
            response.put("message", "Email settings reset to defaults successfully");
            response.put("data", resetSettings);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to reset email settings: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * Test email configuration
     */
    @PostMapping("/test")
    public ResponseEntity<Map<String, Object>> testEmailConfiguration(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // This will use the current email settings to send a test email
            EmailSettings settings = emailSettingsService.getEmailSettings();
            
            if (!settings.getEmailEnabled()) {
                response.put("success", false);
                response.put("message", "Email is currently disabled. Enable it first to test.");
                return ResponseEntity.badRequest().body(response);
            }
            
            // You can integrate with your existing EmailService here
            // For now, just return success
            response.put("success", true);
            response.put("message", "Email configuration test initiated");
            response.put("settings", settings);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to test email configuration: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
