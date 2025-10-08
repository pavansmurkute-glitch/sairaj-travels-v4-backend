package com.sairajtravels.site.controller;

import com.sairajtravels.site.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class EmailTestController {

    private final EmailService emailService;
    private final String fromAddress;

    public EmailTestController(EmailService emailService,
                              @Value("${app.notification.from:${spring.mail.username}}") String fromAddress) {
        this.emailService = emailService;
        this.fromAddress = fromAddress;
    }

    @PostMapping("/email")
    public ResponseEntity<String> testEmail(@RequestParam String to, @RequestParam(required = false) String subject) {
        try {
            System.out.println("=== EMAIL TEST REQUESTED ===");
            System.out.println("To: " + to);
            System.out.println("From: " + fromAddress);
            
            String testSubject = subject != null ? subject : "Test Email from Sairaj Travels";
            String testHtml = """
                <html>
                <body style="font-family: Arial, sans-serif;">
                    <h2 style="color: #2563eb;">Test Email</h2>
                    <p>This is a test email from Sairaj Travels admin panel.</p>
                    <p>If you receive this email, the email configuration is working correctly.</p>
                    <p>From: %s</p>
                    <p>To: %s</p>
                    <p>Time: %s</p>
                </body>
                </html>
                """.formatted(fromAddress, to, java.time.LocalDateTime.now());
            
            String testText = "Test Email from Sairaj Travels\n\nThis is a test email to verify email configuration.\n\nFrom: " + fromAddress + "\nTo: " + to + "\nTime: " + java.time.LocalDateTime.now();
            
            emailService.sendHtmlEmail(to, testSubject, testHtml, testText);
            
            System.out.println("Email test request completed for: " + to);
            return ResponseEntity.ok("Test email sent to: " + to + " at " + java.time.LocalDateTime.now());
        } catch (Exception e) {
            System.err.println("Email test failed: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to send test email: " + e.getMessage());
        }
    }
    
    @GetMapping("/email-config")
    public ResponseEntity<String> getEmailConfig() {
        try {
            return ResponseEntity.ok("📧 SendGrid Email Configuration:\n" +
                "From: " + fromAddress + "\n" +
                "Host: " + System.getenv("EMAIL_HOST") + "\n" +
                "Port: " + System.getenv("EMAIL_PORT") + "\n" +
                "Username: " + System.getenv("SENDGRID_USERNAME") + "\n" +
                "SendGrid API Key Set: " + (System.getenv("SENDGRID_API_KEY") != null && !System.getenv("SENDGRID_API_KEY").isEmpty()) + "\n" +
                "Email Enabled: " + emailService.isEmailEnabled() + "\n" +
                "Time: " + java.time.LocalDateTime.now());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error getting config: " + e.getMessage());
        }
    }
    
    @GetMapping("/test-sendgrid")
    public ResponseEntity<String> testSendGrid() {
        try {
            String testSubject = "SendGrid Test Email from Sairaj Travels";
            String testHtml = """
                <html>
                <body style="font-family: Arial, sans-serif;">
                    <h2 style="color: #2563eb;">🎉 SendGrid Test Email</h2>
                    <p>This is a test email sent via SendGrid from Sairaj Travels backend.</p>
                    <p>If you receive this email, SendGrid configuration is working correctly!</p>
                    <p><strong>From:</strong> %s</p>
                    <p><strong>Time:</strong> %s</p>
                    <hr>
                    <p style="color: #666; font-size: 12px;">This is an automated test email.</p>
                </body>
                </html>
                """.formatted(fromAddress, java.time.LocalDateTime.now());
            
            String testText = "SendGrid Test Email from Sairaj Travels\n\nThis is a test email sent via SendGrid.\n\nFrom: " + fromAddress + "\nTime: " + java.time.LocalDateTime.now();
            
            emailService.sendHtmlEmail(fromAddress, testSubject, testHtml, testText);
            
            return ResponseEntity.ok("✅ SendGrid test email sent to: " + fromAddress + " at " + java.time.LocalDateTime.now());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ SendGrid test failed: " + e.getMessage());
        }
    }
}
