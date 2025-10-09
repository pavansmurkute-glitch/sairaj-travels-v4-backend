package com.sairajtravels.site.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "email_settings")
public class EmailSettings {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "email_enabled", nullable = false)
    private Boolean emailEnabled = true;
    
    @Column(name = "smtp_host", length = 255)
    private String smtpHost;
    
    @Column(name = "smtp_port")
    private Integer smtpPort;
    
    @Column(name = "smtp_username", length = 255)
    private String smtpUsername;
    
    @Column(name = "smtp_password", length = 500)
    private String smtpPassword;
    
    @Column(name = "from_email", length = 255)
    private String fromEmail;
    
    @Column(name = "admin_email", length = 255)
    private String adminEmail;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "updated_by", length = 255)
    private String updatedBy;
    
    // Constructors
    public EmailSettings() {
        this.createdAt = LocalDateTime.now();
        this.emailEnabled = true;
    }
    
    public EmailSettings(Boolean emailEnabled, String smtpHost, Integer smtpPort, 
                        String smtpUsername, String smtpPassword, String fromEmail, 
                        String adminEmail, String updatedBy) {
        this();
        this.emailEnabled = emailEnabled;
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
        this.smtpUsername = smtpUsername;
        this.smtpPassword = smtpPassword;
        this.fromEmail = fromEmail;
        this.adminEmail = adminEmail;
        this.updatedBy = updatedBy;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Boolean getEmailEnabled() {
        return emailEnabled;
    }
    
    public void setEmailEnabled(Boolean emailEnabled) {
        this.emailEnabled = emailEnabled;
    }
    
    public String getSmtpHost() {
        return smtpHost;
    }
    
    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }
    
    public Integer getSmtpPort() {
        return smtpPort;
    }
    
    public void setSmtpPort(Integer smtpPort) {
        this.smtpPort = smtpPort;
    }
    
    public String getSmtpUsername() {
        return smtpUsername;
    }
    
    public void setSmtpUsername(String smtpUsername) {
        this.smtpUsername = smtpUsername;
    }
    
    public String getSmtpPassword() {
        return smtpPassword;
    }
    
    public void setSmtpPassword(String smtpPassword) {
        this.smtpPassword = smtpPassword;
    }
    
    public String getFromEmail() {
        return fromEmail;
    }
    
    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }
    
    public String getAdminEmail() {
        return adminEmail;
    }
    
    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
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
    
    public String getUpdatedBy() {
        return updatedBy;
    }
    
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    @Override
    public String toString() {
        return "EmailSettings{" +
                "id=" + id +
                ", emailEnabled=" + emailEnabled +
                ", smtpHost='" + smtpHost + '\'' +
                ", smtpPort=" + smtpPort +
                ", fromEmail='" + fromEmail + '\'' +
                ", adminEmail='" + adminEmail + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
