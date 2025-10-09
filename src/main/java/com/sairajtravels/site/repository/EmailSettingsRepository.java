package com.sairajtravels.site.repository;

import com.sairajtravels.site.entity.EmailSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailSettingsRepository extends JpaRepository<EmailSettings, Long> {
    
    /**
     * Find the active email settings (there should only be one active record)
     * @return Optional EmailSettings
     */
    @Query("SELECT e FROM EmailSettings e ORDER BY e.updatedAt DESC, e.createdAt DESC")
    Optional<EmailSettings> findActiveEmailSettings();
    
    /**
     * Check if email is enabled
     * @return true if email is enabled, false otherwise
     */
    @Query("SELECT COALESCE(e.emailEnabled, true) FROM EmailSettings e ORDER BY e.updatedAt DESC, e.createdAt DESC")
    Optional<Boolean> isEmailEnabled();
    
    /**
     * Get the latest email settings
     * @return EmailSettings or null if none exist
     */
    default EmailSettings getLatestSettings() {
        return findActiveEmailSettings().orElse(null);
    }
}
