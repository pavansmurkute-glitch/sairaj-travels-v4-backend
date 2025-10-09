package com.sairajtravels.site.service;

import com.sairajtravels.site.entity.EmailSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.concurrent.CompletableFuture;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private EmailSettingsService emailSettingsService;
    
    @Value("${spring.mail.username:admin@sairajtravels.com}")
    private String defaultFromEmail;
    
    @Value("${app.email.enabled:true}")
    private boolean defaultEmailEnabled;
    
    public boolean isEmailEnabled() {
        try {
            return emailSettingsService.isEmailEnabled();
        } catch (Exception e) {
            System.err.println("Error checking email settings from database, using default: " + e.getMessage());
            return defaultEmailEnabled;
        }
    }
    
    /**
     * Get the from email address from database settings or use default
     */
    private String getFromEmail() {
        try {
            EmailSettings settings = emailSettingsService.getEmailConfiguration();
            return settings.getFromEmail() != null ? settings.getFromEmail() : defaultFromEmail;
        } catch (Exception e) {
            System.err.println("Error getting from email from database, using default: " + e.getMessage());
            return defaultFromEmail;
        }
    }
    
    @Value("${app.frontend.url:http://localhost:5173}")
    private String frontendUrl;
    
    @Async
    public CompletableFuture<Void> sendTemporaryPassword(String toEmail, String fullName, String username, String tempPassword) {
        if (!isEmailEnabled()) {
            System.out.println("📧 Email service disabled - temporary password for " + username + ": " + tempPassword);
            return CompletableFuture.completedFuture(null);
        }
        
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            helper.setFrom(getFromEmail());
            helper.setTo(toEmail);
            helper.setSubject("Welcome to Sairaj Travels Admin Panel - Temporary Password");
            
            String htmlContent = buildTemporaryPasswordEmail(fullName, username, tempPassword);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            System.out.println("✅ Temporary password email sent successfully to: " + toEmail);
        } catch (Exception e) {
            System.err.println("⚠️ Email service unavailable - temporary password for " + username + ": " + tempPassword);
            System.err.println("Email error: " + e.getMessage());
            // Don't throw exception - just log the credentials for manual sending
        }
        return CompletableFuture.completedFuture(null);
    }
    
    @Async
    public CompletableFuture<Void> sendPasswordResetEmail(String toEmail, String fullName, String resetToken) {
        if (!isEmailEnabled()) {
            System.out.println("📧 Email service disabled - password reset token for " + fullName + ": " + resetToken);
            return CompletableFuture.completedFuture(null);
        }
        
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            helper.setFrom(getFromEmail());
            helper.setTo(toEmail);
            helper.setSubject("Sairaj Travels Admin - Password Reset Request");
            
            String resetLink = frontendUrl + "/admin/reset-password?token=" + resetToken;
            String htmlContent = buildPasswordResetEmail(fullName, resetLink);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            System.out.println("✅ Password reset email sent successfully to: " + toEmail);
        } catch (Exception e) {
            System.err.println("⚠️ Email service unavailable - password reset token for " + fullName + ": " + resetToken);
            System.err.println("Reset link: " + frontendUrl + "/admin/reset-password?token=" + resetToken);
            System.err.println("Email error: " + e.getMessage());
            // Don't throw exception - just log the reset token for manual sending
        }
        return CompletableFuture.completedFuture(null);
    }
    
    public void sendPasswordChangeNotification(String toEmail, String fullName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(getFromEmail());
            message.setTo(toEmail);
            message.setSubject("Sairaj Travels Admin - Password Changed Successfully");
            message.setText(buildPasswordChangeNotification(fullName));
            
            mailSender.send(message);
        } catch (Exception e) {
            // Don't throw exception for notification emails
            System.err.println("Failed to send password change notification: " + e.getMessage());
        }
    }
    
    private String buildTemporaryPasswordEmail(String fullName, String username, String tempPassword) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background: #2563eb; color: white; padding: 20px; text-align: center; border-radius: 8px 8px 0 0; }
                    .content { background: #f8fafc; padding: 30px; border-radius: 0 0 8px 8px; }
                    .credentials { background: white; padding: 20px; border-radius: 8px; margin: 20px 0; border-left: 4px solid #2563eb; }
                    .button { display: inline-block; background: #2563eb; color: white; padding: 12px 24px; text-decoration: none; border-radius: 6px; margin: 20px 0; }
                    .warning { background: #fef3cd; border: 1px solid #fde68a; padding: 15px; border-radius: 6px; margin: 20px 0; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🚐 Sairaj Travels</h1>
                        <h2>Welcome to Admin Panel</h2>
                    </div>
                    <div class="content">
                        <h3>Hello %s,</h3>
                        <p>Welcome to the Sairaj Travels Admin Panel! Your account has been created successfully.</p>
                        
                        <div class="credentials">
                            <h4>Your Login Credentials:</h4>
                            <p><strong>Username:</strong> %s</p>
                            <p><strong>Temporary Password:</strong> <code>%s</code></p>
                            <p><strong>Login URL:</strong> <a href="%s/admin/login">%s/admin/login</a></p>
                        </div>
                        
                        <div class="warning">
                            <p><strong>⚠️ Important Security Notice:</strong></p>
                            <p>This is a temporary password. You will be required to change it upon your first login for security reasons.</p>
                        </div>
                        
                        <a href="%s/admin/login" class="button">Login to Admin Panel</a>
                        
                        <p>If you have any questions or need assistance, please contact the system administrator.</p>
                        
                        <hr style="margin: 30px 0; border: none; border-top: 1px solid #e5e7eb;">
                        <p style="font-size: 12px; color: #6b7280;">
                            This is an automated message from Sairaj Travels Admin System. Please do not reply to this email.
                        </p>
                    </div>
                </div>
            </body>
            </html>
            """, fullName, username, tempPassword, frontendUrl, frontendUrl, frontendUrl);
    }
    
    private String buildPasswordResetEmail(String fullName, String resetLink) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background: #dc2626; color: white; padding: 20px; text-align: center; border-radius: 8px 8px 0 0; }
                    .content { background: #f8fafc; padding: 30px; border-radius: 0 0 8px 8px; }
                    .button { display: inline-block; background: #dc2626; color: white; padding: 12px 24px; text-decoration: none; border-radius: 6px; margin: 20px 0; }
                    .warning { background: #fef3cd; border: 1px solid #fde68a; padding: 15px; border-radius: 6px; margin: 20px 0; }
                    .code { background: white; padding: 15px; border-radius: 6px; font-family: monospace; word-break: break-all; border: 1px solid #e5e7eb; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🚐 Sairaj Travels</h1>
                        <h2>Password Reset Request</h2>
                    </div>
                    <div class="content">
                        <h3>Hello %s,</h3>
                        <p>We received a request to reset your password for the Sairaj Travels Admin Panel.</p>
                        
                        <p>Click the button below to reset your password:</p>
                        <a href="%s" class="button">Reset Password</a>
                        
                        <p>Or copy and paste this link in your browser:</p>
                        <div class="code">%s</div>
                        
                        <div class="warning">
                            <p><strong>⚠️ Security Notice:</strong></p>
                            <ul>
                                <li>This link will expire in 1 hour for security reasons</li>
                                <li>If you didn't request this reset, please ignore this email</li>
                                <li>Your password will remain unchanged until you create a new one</li>
                            </ul>
                        </div>
                        
                        <p>If you're having trouble with the button above, copy and paste the URL into your web browser.</p>
                        
                        <hr style="margin: 30px 0; border: none; border-top: 1px solid #e5e7eb;">
                        <p style="font-size: 12px; color: #6b7280;">
                            This is an automated message from Sairaj Travels Admin System. Please do not reply to this email.<br>
                            If you didn't request this password reset, please contact the system administrator immediately.
                        </p>
                    </div>
                </div>
            </body>
            </html>
            """, fullName, resetLink, resetLink);
    }
    
    private String buildPasswordChangeNotification(String fullName) {
        return String.format("""
            Hello %s,
            
            This is a confirmation that your password for the Sairaj Travels Admin Panel has been successfully changed.
            
            Change Details:
            - Date: %s
            - Account: %s
            
            If you did not make this change, please contact the system administrator immediately.
            
            For security reasons:
            - Always use strong, unique passwords
            - Never share your login credentials
            - Log out when finished using the system
            
            Thank you,
            Sairaj Travels Admin System
            
            ---
            This is an automated message. Please do not reply to this email.
            """, fullName, java.time.LocalDateTime.now().toString(), fullName);
    }
    
    // Backward compatibility methods for existing services
    
    public void sendHtmlEmail(String toEmail, String subject, String htmlContent, String fallbackText) {
        if (!isEmailEnabled()) {
            System.out.println("📧 Email service disabled - would send to: " + toEmail + " | Subject: " + subject);
            return;
        }
        
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            helper.setFrom(getFromEmail());
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(fallbackText, htmlContent);
            
            mailSender.send(message);
            System.out.println("✅ HTML email sent successfully to: " + toEmail);
        } catch (Exception e) {
            System.err.println("⚠️ Email service unavailable - failed to send HTML email to: " + toEmail);
            System.err.println("Subject: " + subject);
            System.err.println("Email error: " + e.getMessage());
            // Don't throw exception - just log the failure
        }
    }
    
    public void notifyAdmin(String subject, String htmlContent, String fallbackText) {
        if (!isEmailEnabled()) {
            System.out.println("📧 Email service disabled - admin notification: " + subject);
            return;
        }
        
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            helper.setFrom(getFromEmail());
            helper.setTo(getFromEmail()); // Send to admin email
            helper.setSubject("Admin Notification: " + subject);
            helper.setText(fallbackText, htmlContent);
            
            mailSender.send(message);
            System.out.println("✅ Admin notification sent successfully");
        } catch (Exception e) {
            // Don't throw exception for admin notifications to avoid breaking business logic
            System.err.println("Failed to send admin notification: " + e.getMessage());
            System.err.println("Failed messages: " + e.getMessage());
        }
    }
}