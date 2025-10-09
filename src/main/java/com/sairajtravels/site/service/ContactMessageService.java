package com.sairajtravels.site.service;

import com.sairajtravels.site.entity.ContactMessage;
import com.sairajtravels.site.repository.ContactMessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactMessageService {

    private final ContactMessageRepository repository;
    private final EmailService emailService;

    public ContactMessageService(ContactMessageRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    public ContactMessage saveMessage(ContactMessage message) {
        // save to DB first
        ContactMessage saved = repository.save(message);

        // Send email notifications (non-blocking - don't fail if emails fail)
        sendContactNotifications(saved);

        return saved;
    }

    private void sendContactNotifications(ContactMessage message) {
        try {
            System.out.println("=== SENDING CONTACT MESSAGE NOTIFICATIONS ===");
            System.out.println("Contact ID: " + message.getId());
            System.out.println("Name: " + message.getName());
            System.out.println("Email: " + message.getEmail());
            
            // 1) Client confirmation (if email provided) - don't fail if this fails
            if (message.getEmail() != null && !message.getEmail().isBlank()) {
                try {
                    String subject = "Sairaj Travels — We received your message";
                    String plain = buildClientText(message);
                    String html = buildClientHtml(message);
                    emailService.sendHtmlEmail(message.getEmail(), subject, html, plain);
                    System.out.println("✅ Customer confirmation email sent successfully");
                } catch (Exception emailError) {
                    System.err.println("⚠️ Failed to send customer confirmation email (message still saved): " + emailError.getMessage());
                    emailError.printStackTrace();
                }
            } else {
                System.out.println("ℹ️ No customer email provided - skipping customer notification");
            }

            // 2) Admin notification - don't fail if this fails
            try {
                String adminSubject = "New Contact Message from " + (message.getName() == null ? "Unknown" : message.getName());
                String adminPlain = buildAdminText(message);
                String adminHtml = buildAdminHtml(message);
                emailService.notifyAdmin(adminSubject, adminHtml, adminPlain);
                System.out.println("✅ Admin notification email sent successfully");
            } catch (Exception adminEmailError) {
                System.err.println("⚠️ Failed to send admin notification email (message still saved): " + adminEmailError.getMessage());
                adminEmailError.printStackTrace();
            }

            System.out.println("=== CONTACT MESSAGE NOTIFICATIONS COMPLETED ===");

        } catch (Exception e) {
            System.err.println("❌ Critical error in contact notifications: " + e.getMessage());
            e.printStackTrace();
            // Don't rethrow - message should still be saved even if emails fail
        }
    }

    // other methods (getAll, getById, delete) remain unchanged
    public List<ContactMessage> getAllMessages() { return repository.findAll(); }
    public Optional<ContactMessage> getMessageById(Integer id) { return repository.findById(id); }
    public void deleteMessage(Integer id) { repository.deleteById(id); }

    // ---- helpers for email content ----
    private String buildClientText(ContactMessage m) {
        String name = (m.getName() == null ? "Customer" : m.getName());
        return "Dear " + name + ",\n\n"
                + "Thank you for contacting Sairaj Travels. We have received your message and will get back to you shortly.\n\n"
                + "Message summary:\n"
                + "Phone: " + (m.getPhone() == null ? "N/A" : m.getPhone()) + "\n"
                + "Message: " + (m.getMessage() == null ? "" : m.getMessage()) + "\n\n"
                + "Warm regards,\nSairaj Travels";
    }

    private String buildClientHtml(ContactMessage m) {
        String name = (m.getName() == null ? "Customer" : escapeHtml(m.getName()));
        return "<html><body>"
                + "<p>Dear <strong>" + name + "</strong>,</p>"
                + "<p>Thank you for contacting <strong>Sairaj Travels</strong>. We have received your message and will get back to you shortly.</p>"
                + "<h4>Message summary</h4>"
                + "<p><strong>Phone:</strong> " + escapeHtml(m.getPhone()) + "</p>"
                + "<p><strong>Message:</strong><br/>" + nl2br(escapeHtml(m.getMessage())) + "</p>"
                + "<p>Warm regards,<br/>Sairaj Travels</p>"
                + "</body></html>";
    }

    private String buildAdminText(ContactMessage m) {
        return "New contact message\n\n"
                + "ID: " + m.getId() + "\n"
                + "Name: " + (m.getName() == null ? "N/A" : m.getName()) + "\n"
                + "Email: " + (m.getEmail() == null ? "N/A" : m.getEmail()) + "\n"
                + "Phone: " + (m.getPhone() == null ? "N/A" : m.getPhone()) + "\n"
                + "Message:\n" + (m.getMessage() == null ? "" : m.getMessage()) + "\n";
    }

    private String buildAdminHtml(ContactMessage m) {
        return "<html><body>"
                + "<h3>New contact message</h3>"
                + "<p><strong>ID:</strong> " + m.getId() + "</p>"
                + "<p><strong>Name:</strong> " + escapeHtml(m.getName()) + "</p>"
                + "<p><strong>Email:</strong> " + escapeHtml(m.getEmail()) + "</p>"
                + "<p><strong>Phone:</strong> " + escapeHtml(m.getPhone()) + "</p>"
                + "<h4>Message</h4>"
                + "<p>" + nl2br(escapeHtml(m.getMessage())) + "</p>"
                + "</body></html>";
    }

    // small helpers
    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }
    private String nl2br(String s) {
        if (s == null) return "";
        return s.replace("\n", "<br/>");
    }
}
