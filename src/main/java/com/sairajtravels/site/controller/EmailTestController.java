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
    
    @GetMapping("/test-hardcoded-email")
    public ResponseEntity<String> testHardcodedEmail() {
        try {
            System.out.println("=== HARDCODED EMAIL TEST STARTED ===");
            System.out.println("Timestamp: " + java.time.LocalDateTime.now());
            System.out.println("From Address: " + fromAddress);
            System.out.println("Email Service Enabled: " + emailService.isEmailEnabled());
            System.out.println("Environment Variables:");
            System.out.println("  EMAIL_HOST: " + System.getenv("EMAIL_HOST"));
            System.out.println("  EMAIL_PORT: " + System.getenv("EMAIL_PORT"));
            System.out.println("  SENDGRID_USERNAME: " + System.getenv("SENDGRID_USERNAME"));
            System.out.println("  SENDGRID_API_KEY length: " + (System.getenv("SENDGRID_API_KEY") != null ? System.getenv("SENDGRID_API_KEY").length() : "null"));
            
            // Test hardcoded contact form email
            String customerEmail = "pavansmurkute@gmail.com";
            String customerSubject = "Test Contact Form - Sairaj Travels";
            String customerHtml = """
                <html>
                <body style="font-family: Arial, sans-serif;">
                    <h2 style="color: #2563eb;">Thank you for contacting Sairaj Travels!</h2>
                    <p>We have received your message and will get back to you soon.</p>
                    <p><strong>Your Message:</strong> This is a hardcoded test message</p>
                    <p><strong>Time:</strong> %s</p>
                    <hr>
                    <p style="color: #666; font-size: 12px;">This is an automated response from Sairaj Travels.</p>
                </body>
                </html>
                """.formatted(java.time.LocalDateTime.now());
            
            String customerText = "Thank you for contacting Sairaj Travels!\n\nWe have received your message and will get back to you soon.\n\nYour Message: This is a hardcoded test message\nTime: " + java.time.LocalDateTime.now();
            
            System.out.println("Attempting to send customer email to: " + customerEmail);
            emailService.sendHtmlEmail(customerEmail, customerSubject, customerHtml, customerText);
            System.out.println("✅ Customer email sent successfully");
            
            // Test admin notification
            String adminSubject = "Test Admin Notification - Contact Form";
            String adminHtml = """
                <html>
                <body style="font-family: Arial, sans-serif;">
                    <h2 style="color: #dc2626;">New Contact Message - Test</h2>
                    <p><strong>From:</strong> Test User</p>
                    <p><strong>Email:</strong> pavansmurkute@gmail.com</p>
                    <p><strong>Phone:</strong> +919921793267</p>
                    <p><strong>Message:</strong> This is a hardcoded test message</p>
                    <p><strong>Time:</strong> %s</p>
                </body>
                </html>
                """.formatted(java.time.LocalDateTime.now());
            
            String adminText = "New Contact Message - Test\n\nFrom: Test User\nEmail: pavansmurkute@gmail.com\nPhone: +919921793267\nMessage: This is a hardcoded test message\nTime: " + java.time.LocalDateTime.now();
            
            System.out.println("Attempting to send admin notification to: " + fromAddress);
            emailService.notifyAdmin(adminSubject, adminHtml, adminText);
            System.out.println("✅ Admin notification sent successfully");
            
            System.out.println("=== HARDCODED EMAIL TEST COMPLETED ===");
            
            return ResponseEntity.ok("✅ Hardcoded email test completed! Check server logs for details. Emails sent to: " + customerEmail + " and " + fromAddress);
            
        } catch (Exception e) {
            System.err.println("=== HARDCODED EMAIL TEST FAILED ===");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.err.println("=== END ERROR LOG ===");
            
            return ResponseEntity.status(500).body("❌ Hardcoded email test failed: " + e.getMessage() + "\n\nFull error details logged to server console.");
        }
    }
}
