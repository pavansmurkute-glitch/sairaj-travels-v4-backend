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
        // save to DB
        ContactMessage saved = repository.save(message);

        // 1) Client confirmation (if email provided)
        if (saved.getEmail() != null && !saved.getEmail().isBlank()) {
            String subject = "Sairaj Travels — We received your message";
            String plain = buildClientText(saved);
            String html = buildClientHtml(saved);
            emailService.sendHtmlEmail(saved.getEmail(), subject, html, plain);
        }

        // 2) Admin notification
        String adminSubject = "New Contact Message from " + (saved.getName() == null ? "Unknown" : saved.getName());
        String adminPlain = buildAdminText(saved);
        String adminHtml = buildAdminHtml(saved);
        emailService.notifyAdmin(adminSubject, adminHtml, adminPlain);

        return saved;
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
