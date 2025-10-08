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
            System.out.println("=== HARDCODED EMAIL TEST WITH DIRECT CONFIGURATION ===");
            System.out.println("Timestamp: " + java.time.LocalDateTime.now());
            
            // Hardcoded SendGrid configuration for testing
            String hardcodedHost = "smtp.sendgrid.net";
            String hardcodedPort = "2525"; // Using port 2525 as requested
            String hardcodedUsername = "apikey";
            String hardcodedPassword = System.getenv("SENDGRID_API_KEY"); // Get from environment
            String hardcodedFromEmail = "PavansMurkute@gmail.com";
            
            System.out.println("Hardcoded Configuration:");
            System.out.println("  Host: " + hardcodedHost);
            System.out.println("  Port: " + hardcodedPort);
            System.out.println("  Username: " + hardcodedUsername);
            System.out.println("  Password length: " + (hardcodedPassword != null ? hardcodedPassword.length() : "null"));
            System.out.println("  From Email: " + hardcodedFromEmail);
            
            // Create custom JavaMailSender with hardcoded settings
            org.springframework.mail.javamail.JavaMailSenderImpl customMailSender = new org.springframework.mail.javamail.JavaMailSenderImpl();
            customMailSender.setHost(hardcodedHost);
            customMailSender.setPort(Integer.parseInt(hardcodedPort));
            customMailSender.setUsername(hardcodedUsername);
            customMailSender.setPassword(hardcodedPassword);
            
            // Set SMTP properties
            java.util.Properties props = customMailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.starttls.required", "true");
            props.put("mail.smtp.ssl.trust", hardcodedHost);
            props.put("mail.smtp.connectiontimeout", "30000");
            props.put("mail.smtp.timeout", "30000");
            props.put("mail.smtp.writetimeout", "30000");
            props.put("mail.debug", "true"); // Enable debug for troubleshooting
            
            // Test hardcoded contact form email
            String customerEmail = "pavansmurkute@gmail.com";
            String customerSubject = "Test Contact Form - Sairaj Travels (Hardcoded Config)";
            String customerHtml = """
                <html>
                <body style="font-family: Arial, sans-serif;">
                    <h2 style="color: #2563eb;">Thank you for contacting Sairaj Travels!</h2>
                    <p>We have received your message and will get back to you soon.</p>
                    <p><strong>Your Message:</strong> This is a hardcoded test message with direct SendGrid configuration</p>
                    <p><strong>Time:</strong> %s</p>
                    <p><strong>Configuration:</strong> Host=%s, Port=%s</p>
                    <hr>
                    <p style="color: #666; font-size: 12px;">This is an automated response from Sairaj Travels.</p>
                </body>
                </html>
                """.formatted(java.time.LocalDateTime.now(), hardcodedHost, hardcodedPort);
            
            String customerText = "Thank you for contacting Sairaj Travels!\n\nWe have received your message and will get back to you soon.\n\nYour Message: This is a hardcoded test message with direct SendGrid configuration\nTime: " + java.time.LocalDateTime.now() + "\nConfig: Host=" + hardcodedHost + ", Port=" + hardcodedPort;
            
            System.out.println("Attempting to send customer email to: " + customerEmail);
            System.out.println("Using hardcoded SendGrid configuration...");
            
            // Send email using custom configuration
            jakarta.mail.internet.MimeMessage message = customMailSender.createMimeMessage();
            jakarta.mail.internet.MimeMessageHelper helper = new jakarta.mail.internet.MimeMessageHelper(message, true);
            helper.setFrom(hardcodedFromEmail);
            helper.setTo(customerEmail);
            helper.setSubject(customerSubject);
            helper.setText(customerText, customerHtml);
            
            customMailSender.send(message);
            System.out.println("✅ Customer email sent successfully with hardcoded config");
            
            // Test admin notification
            String adminSubject = "Test Admin Notification - Contact Form (Hardcoded)";
            String adminHtml = """
                <html>
                <body style="font-family: Arial, sans-serif;">
                    <h2 style="color: #dc2626;">New Contact Message - Test (Hardcoded Config)</h2>
                    <p><strong>From:</strong> Test User</p>
                    <p><strong>Email:</strong> pavansmurkute@gmail.com</p>
                    <p><strong>Phone:</strong> +919921793267</p>
                    <p><strong>Message:</strong> This is a hardcoded test message</p>
                    <p><strong>Time:</strong> %s</p>
                    <p><strong>Config Used:</strong> Host=%s, Port=%s</p>
                </body>
                </html>
                """.formatted(java.time.LocalDateTime.now(), hardcodedHost, hardcodedPort);
            
            String adminText = "New Contact Message - Test (Hardcoded Config)\n\nFrom: Test User\nEmail: pavansmurkute@gmail.com\nPhone: +919921793267\nMessage: This is a hardcoded test message\nTime: " + java.time.LocalDateTime.now() + "\nConfig: Host=" + hardcodedHost + ", Port=" + hardcodedPort;
            
            System.out.println("Attempting to send admin notification to: " + hardcodedFromEmail);
            
            // Send admin notification using custom configuration
            jakarta.mail.internet.MimeMessage adminMessage = customMailSender.createMimeMessage();
            jakarta.mail.internet.MimeMessageHelper adminHelper = new jakarta.mail.internet.MimeMessageHelper(adminMessage, true);
            adminHelper.setFrom(hardcodedFromEmail);
            adminHelper.setTo(hardcodedFromEmail);
            adminHelper.setSubject("Admin Notification: " + adminSubject);
            adminHelper.setText(adminText, adminHtml);
            
            customMailSender.send(adminMessage);
            System.out.println("✅ Admin notification sent successfully with hardcoded config");
            
            System.out.println("=== HARDCODED EMAIL TEST COMPLETED ===");
            
            return ResponseEntity.ok("✅ Hardcoded email test completed with direct SendGrid configuration!\n" +
                "Configuration used:\n" +
                "- Host: " + hardcodedHost + "\n" +
                "- Port: " + hardcodedPort + "\n" +
                "- Username: " + hardcodedUsername + "\n" +
                "- From: " + hardcodedFromEmail + "\n" +
                "Emails sent to: " + customerEmail + " and " + hardcodedFromEmail + "\n" +
                "Check server logs for detailed debug information.");
            
        } catch (Exception e) {
            System.err.println("=== HARDCODED EMAIL TEST FAILED ===");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.err.println("=== END ERROR LOG ===");
            
            return ResponseEntity.status(500).body("❌ Hardcoded email test failed: " + e.getMessage() + 
                "\n\nConfiguration attempted:\n" +
                "- Host: smtp.sendgrid.net\n" +
                "- Port: 2525\n" +
                "- Username: apikey\n" +
                "\nFull error details logged to server console.");
        }
    }
}
