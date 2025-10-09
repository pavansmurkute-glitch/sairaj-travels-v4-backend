package com.sairajtravels.site.service;

import com.sairajtravels.site.entity.EmailSettings;
import com.sairajtravels.site.repository.EmailSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class EmailSettingsService {
    
    @Autowired
    private EmailSettingsRepository emailSettingsRepository;
    
    @Value("${spring.mail.host:smtp.sendgrid.net}")
    private String defaultSmtpHost;
    
    @Value("${spring.mail.port:2525}")
    private String defaultSmtpPort;
    
    @Value("${spring.mail.username:apikey}")
    private String defaultSmtpUsername;
    
    @Value("${app.notification.from:PavansMurkute@gmail.com}")
    private String defaultFromEmail;
    
    @Value("${app.notification.admin-email:admin@sairajtravels.com}")
    private String defaultAdminEmail;
    
    /**
     * Get current email settings, create default if none exist
     */
    public EmailSettings getEmailSettings() {
        EmailSettings settings = emailSettingsRepository.getLatestSettings();
        
        if (settings == null) {
            // Create default settings if none exist
            settings = createDefaultSettings();
        }
        
        return settings;
    }
    
    /**
     * Create default email settings
     */
    private EmailSettings createDefaultSettings() {
        EmailSettings defaultSettings = new EmailSettings();
        defaultSettings.setEmailEnabled(true);
        defaultSettings.setSmtpHost(defaultSmtpHost);
        defaultSettings.setSmtpPort(Integer.parseInt(defaultSmtpPort));
        defaultSettings.setSmtpUsername(defaultSmtpUsername);
        defaultSettings.setFromEmail(defaultFromEmail);
        defaultSettings.setAdminEmail(defaultAdminEmail);
        defaultSettings.setUpdatedBy("System");
        
        return emailSettingsRepository.save(defaultSettings);
    }
    
    /**
     * Update email settings
     */
    public EmailSettings updateEmailSettings(EmailSettings emailSettings, String updatedBy) {
        EmailSettings existingSettings = getEmailSettings();
        
        if (existingSettings != null) {
            // Update existing settings
            existingSettings.setEmailEnabled(emailSettings.getEmailEnabled());
            existingSettings.setSmtpHost(emailSettings.getSmtpHost());
            existingSettings.setSmtpPort(emailSettings.getSmtpPort());
            existingSettings.setSmtpUsername(emailSettings.getSmtpUsername());
            existingSettings.setFromEmail(emailSettings.getFromEmail());
            existingSettings.setAdminEmail(emailSettings.getAdminEmail());
            existingSettings.setUpdatedBy(updatedBy);
            
            return emailSettingsRepository.save(existingSettings);
        } else {
            // Create new settings
            emailSettings.setUpdatedBy(updatedBy);
            return emailSettingsRepository.save(emailSettings);
        }
    }
    
    /**
     * Toggle email enabled/disabled
     */
    public EmailSettings toggleEmailEnabled(boolean enabled, String updatedBy) {
        EmailSettings settings = getEmailSettings();
        settings.setEmailEnabled(enabled);
        settings.setUpdatedBy(updatedBy);
        
        return emailSettingsRepository.save(settings);
    }
    
    /**
     * Check if email is enabled
     */
    public boolean isEmailEnabled() {
        Optional<Boolean> enabled = emailSettingsRepository.isEmailEnabled();
        return enabled.orElse(true); // Default to enabled if no settings found
    }
    
    /**
     * Get email configuration for sending emails
     */
    public EmailSettings getEmailConfiguration() {
        return getEmailSettings();
    }
    
    /**
     * Reset to default settings
     */
    public EmailSettings resetToDefaults(String updatedBy) {
        EmailSettings defaultSettings = new EmailSettings();
        defaultSettings.setEmailEnabled(true);
        defaultSettings.setSmtpHost(defaultSmtpHost);
        defaultSettings.setSmtpPort(Integer.parseInt(defaultSmtpPort));
        defaultSettings.setSmtpUsername(defaultSmtpUsername);
        defaultSettings.setFromEmail(defaultFromEmail);
        defaultSettings.setAdminEmail(defaultAdminEmail);
        defaultSettings.setUpdatedBy(updatedBy);
        
        return updateEmailSettings(defaultSettings, updatedBy);
    }
}
