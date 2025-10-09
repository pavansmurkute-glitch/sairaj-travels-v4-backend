package com.sairajtravels.site.service;

import com.sairajtravels.site.dto.EnquiryDTO;
import com.sairajtravels.site.entity.Enquiry;
import com.sairajtravels.site.repository.EnquiryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EnquiryService {

    private final EnquiryRepository enquiryRepository;
    private final EmailService emailService;

    public EnquiryService(EnquiryRepository enquiryRepository, EmailService emailService) {
        this.enquiryRepository = enquiryRepository;
        this.emailService = emailService;
    }

    private EnquiryDTO convertToDTO(Enquiry enquiry) {
        EnquiryDTO dto = new EnquiryDTO();
        dto.setEnquiryId(enquiry.getEnquiryId());
        dto.setFullName(enquiry.getFullName());
        dto.setPhone(enquiry.getPhone());
        dto.setEmail(enquiry.getEmail());
        dto.setService(enquiry.getService());
        dto.setMessage(enquiry.getMessage());
        dto.setStatus(enquiry.getStatus());
        dto.setCreatedAt(enquiry.getCreatedAt());
        dto.setUpdatedAt(enquiry.getUpdatedAt());
        return dto;
    }

    private Enquiry convertToEntity(EnquiryDTO dto) {
        Enquiry enquiry = new Enquiry();
        enquiry.setEnquiryId(dto.getEnquiryId());
        enquiry.setFullName(dto.getFullName());
        enquiry.setPhone(dto.getPhone());
        enquiry.setEmail(dto.getEmail());
        enquiry.setService(dto.getService());
        enquiry.setMessage(dto.getMessage());
        enquiry.setStatus(dto.getStatus());
        enquiry.setCreatedAt(dto.getCreatedAt());
        enquiry.setUpdatedAt(dto.getUpdatedAt());
        return enquiry;
    }

    public List<EnquiryDTO> getAllEnquiries() {
        return enquiryRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EnquiryDTO> getEnquiriesByStatus(String status) {
        return enquiryRepository.findByStatus(status)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EnquiryDTO> getEnquiriesByService(String service) {
        return enquiryRepository.findByService(service)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public EnquiryDTO getEnquiryById(Integer id) {
        Optional<Enquiry> enquiry = enquiryRepository.findById(id);
        return enquiry.map(this::convertToDTO).orElse(null);
    }

    public EnquiryDTO createEnquiry(EnquiryDTO dto) {
        // Set default values
        if (dto.getStatus() == null || dto.getStatus().isEmpty()) {
            dto.setStatus("PENDING");
        }
        if (dto.getCreatedAt() == null) {
            dto.setCreatedAt(LocalDateTime.now());
        }
        if (dto.getUpdatedAt() == null) {
            dto.setUpdatedAt(LocalDateTime.now());
        }

        Enquiry saved = enquiryRepository.save(convertToEntity(dto));
        EnquiryDTO result = convertToDTO(saved);

        // Send email notifications
        sendEnquiryNotifications(result);

        return result;
    }

    public EnquiryDTO updateEnquiry(Integer id, EnquiryDTO dto) {
        Optional<Enquiry> existingEnquiryOpt = enquiryRepository.findById(id);
        if (existingEnquiryOpt.isPresent()) {
            Enquiry existingEnquiry = existingEnquiryOpt.get();
            
            // Update only the fields that are provided in the DTO
            if (dto.getStatus() != null) {
                existingEnquiry.setStatus(dto.getStatus());
            }
            if (dto.getFullName() != null) {
                existingEnquiry.setFullName(dto.getFullName());
            }
            if (dto.getEmail() != null) {
                existingEnquiry.setEmail(dto.getEmail());
            }
            if (dto.getPhone() != null) {
                existingEnquiry.setPhone(dto.getPhone());
            }
            if (dto.getService() != null) {
                existingEnquiry.setService(dto.getService());
            }
            if (dto.getMessage() != null) {
                existingEnquiry.setMessage(dto.getMessage());
            }
            
            existingEnquiry.setUpdatedAt(LocalDateTime.now());
            
            Enquiry updated = enquiryRepository.save(existingEnquiry);
            EnquiryDTO result = convertToDTO(updated);
            
            // Send email notification for status changes
            if (dto.getStatus() != null && !dto.getStatus().equals(existingEnquiry.getStatus())) {
                sendEnquiryUpdateNotification(result);
            }
            
            return result;
        }
        return null;
    }

    public void deleteEnquiry(Integer id) {
        enquiryRepository.deleteById(id);
    }

    private void sendEnquiryNotifications(EnquiryDTO enquiry) {
        try {
            System.out.println("=== SENDING ENQUIRY NOTIFICATIONS ===");
            System.out.println("Enquiry ID: " + enquiry.getEnquiryId());
            System.out.println("Name: " + enquiry.getFullName());
            System.out.println("Email: " + enquiry.getEmail());
            System.out.println("Service: " + enquiry.getService());
            
            // Send email to customer if email is provided - don't fail if this fails
            if (enquiry.getEmail() != null && !enquiry.getEmail().isEmpty()) {
                try {
                    String customerSubject = "Enquiry Received - Sairaj Travels";
                    String customerHtml = buildCustomerEmailHtml(enquiry);
                    String customerText = buildCustomerEmailText(enquiry);
                    emailService.sendHtmlEmail(enquiry.getEmail(), customerSubject, customerHtml, customerText);
                    System.out.println("✅ Customer enquiry confirmation email sent successfully");
                } catch (Exception emailError) {
                    System.err.println("⚠️ Failed to send customer enquiry confirmation email (enquiry still saved): " + emailError.getMessage());
                    emailError.printStackTrace();
                }
            } else {
                System.out.println("ℹ️ No customer email provided - skipping customer notification");
            }

            // Send email to admin - don't fail if this fails
            try {
                String adminSubject = "New Enquiry - " + enquiry.getFullName() + " (" + enquiry.getService() + ")";
                String adminHtml = buildAdminEmailHtml(enquiry);
                String adminText = buildAdminEmailText(enquiry);
                emailService.notifyAdmin(adminSubject, adminHtml, adminText);
                System.out.println("✅ Admin enquiry notification email sent successfully");
            } catch (Exception adminEmailError) {
                System.err.println("⚠️ Failed to send admin enquiry notification email (enquiry still saved): " + adminEmailError.getMessage());
                adminEmailError.printStackTrace();
            }

            System.out.println("=== ENQUIRY NOTIFICATIONS COMPLETED ===");

        } catch (Exception e) {
            System.err.println("❌ Critical error in enquiry notifications: " + e.getMessage());
            e.printStackTrace();
            // Don't rethrow - enquiry should still be saved even if emails fail
        }
    }

    private String buildCustomerEmailHtml(EnquiryDTO enquiry) {
        return String.format("""
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                    <h2 style="color: #2563eb;">Thank you for your enquiry!</h2>
                    
                    <p>Dear %s,</p>
                    
                    <p>We have received your enquiry and our team will get back to you within 2 hours with a personalized quote.</p>
                    
                    <div style="background: #f8fafc; padding: 20px; border-radius: 8px; margin: 20px 0;">
                        <h3 style="color: #1e40af; margin-top: 0;">Enquiry Details</h3>
                        <p><strong>Enquiry ID:</strong> #%d</p>
                        <p><strong>Service:</strong> %s</p>
                        <p><strong>Phone:</strong> %s</p>
                        <p><strong>Message:</strong> %s</p>
                        <p><strong>Status:</strong> %s</p>
                    </div>
                    
                    <p>Our team will contact you shortly to discuss your requirements and provide you with the best possible quote.</p>
                    
                    <p>For immediate assistance, please contact us at:</p>
                    <ul>
                        <li>Phone: +91 9850748273</li>
                        <li>Email: admin@sairajtravels.com</li>
                    </ul>
                    
                    <p>Thank you for choosing Sairaj Travels!</p>
                    
                    <hr style="margin: 30px 0; border: none; border-top: 1px solid #e5e7eb;">
                    <p style="font-size: 12px; color: #6b7280;">
                        Sairaj Travels - Your Trusted Travel Partner<br>
                        This is an automated email. Please do not reply.
                    </p>
                </div>
            </body>
            </html>
            """, 
            enquiry.getFullName(),
            enquiry.getEnquiryId(),
            enquiry.getService(),
            enquiry.getPhone(),
            enquiry.getMessage(),
            enquiry.getStatus()
        );
    }

    private String buildCustomerEmailText(EnquiryDTO enquiry) {
        return String.format("""
            Thank you for your enquiry!
            
            Dear %s,
            
            We have received your enquiry and our team will get back to you within 2 hours with a personalized quote.
            
            Enquiry Details:
            - Enquiry ID: #%d
            - Service: %s
            - Phone: %s
            - Message: %s
            - Status: %s
            
            Our team will contact you shortly to discuss your requirements and provide you with the best possible quote.
            
            For immediate assistance, please contact us at:
            - Phone: +91 9850748273
            - Email: admin@sairajtravels.com
            
            Thank you for choosing Sairaj Travels!
            
            ---
            Sairaj Travels - Your Trusted Travel Partner
            This is an automated email. Please do not reply.
            """, 
            enquiry.getFullName(),
            enquiry.getEnquiryId(),
            enquiry.getService(),
            enquiry.getPhone(),
            enquiry.getMessage(),
            enquiry.getStatus()
        );
    }

    private String buildAdminEmailHtml(EnquiryDTO enquiry) {
        return String.format("""
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                    <h2 style="color: #dc2626;">New Enquiry Received</h2>
                    
                    <p>A new enquiry has been submitted through the website:</p>
                    
                    <div style="background: #fef2f2; padding: 20px; border-radius: 8px; margin: 20px 0; border-left: 4px solid #dc2626;">
                        <h3 style="color: #dc2626; margin-top: 0;">Enquiry Details</h3>
                        <p><strong>Enquiry ID:</strong> #%d</p>
                        <p><strong>Name:</strong> %s</p>
                        <p><strong>Phone:</strong> %s</p>
                        <p><strong>Email:</strong> %s</p>
                        <p><strong>Service:</strong> %s</p>
                        <p><strong>Message:</strong> %s</p>
                        <p><strong>Status:</strong> %s</p>
                        <p><strong>Submitted At:</strong> %s</p>
                    </div>
                    
                    <p style="color: #dc2626; font-weight: bold;">Action Required: Please contact the customer within 2 hours to provide a quote.</p>
                </div>
            </body>
            </html>
            """, 
            enquiry.getEnquiryId(),
            enquiry.getFullName(),
            enquiry.getPhone(),
            enquiry.getEmail() != null ? enquiry.getEmail() : "Not provided",
            enquiry.getService(),
            enquiry.getMessage(),
            enquiry.getStatus(),
            enquiry.getCreatedAt()
        );
    }

    private String buildAdminEmailText(EnquiryDTO enquiry) {
        return String.format("""
            New Enquiry Received
            
            A new enquiry has been submitted through the website:
            
            Enquiry Details:
            - Enquiry ID: #%d
            - Name: %s
            - Phone: %s
            - Email: %s
            - Service: %s
            - Message: %s
            - Status: %s
            - Submitted At: %s
            
            Action Required: Please contact the customer within 2 hours to provide a quote.
            """, 
            enquiry.getEnquiryId(),
            enquiry.getFullName(),
            enquiry.getPhone(),
            enquiry.getEmail() != null ? enquiry.getEmail() : "Not provided",
            enquiry.getService(),
            enquiry.getMessage(),
            enquiry.getStatus(),
            enquiry.getCreatedAt()
        );
    }

    private void sendEnquiryUpdateNotification(EnquiryDTO enquiry) {
        try {
            // Send email to customer about enquiry status change
            if (enquiry.getEmail() != null && !enquiry.getEmail().isEmpty()) {
                String customerSubject = "Enquiry Status Update - Sairaj Travels";
                String customerHtml = buildEnquiryUpdateEmailHtml(enquiry);
                String customerText = buildEnquiryUpdateEmailText(enquiry);
                emailService.sendHtmlEmail(enquiry.getEmail(), customerSubject, customerHtml, customerText);
            }

        } catch (Exception e) {
            System.err.println("Failed to send enquiry update notification: " + e.getMessage());
        }
    }

    private String buildEnquiryUpdateEmailHtml(EnquiryDTO enquiry) {
        String statusColor = getEnquiryStatusColor(enquiry.getStatus());
        String statusMessage = getEnquiryStatusMessage(enquiry.getStatus());
        
        return String.format("""
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                    <h2 style="color: #2563eb;">Enquiry Status Update</h2>
                    
                    <p>Dear %s,</p>
                    
                    <p>Your enquiry status has been updated. Here are the current details:</p>
                    
                    <div style="background: #f8fafc; padding: 20px; border-radius: 8px; margin: 20px 0;">
                        <h3 style="color: #1e40af; margin-top: 0;">Enquiry Details</h3>
                        <p><strong>Enquiry ID:</strong> #%d</p>
                        <p><strong>Service:</strong> %s</p>
                        <p><strong>Message:</strong> %s</p>
                        <p><strong>Status:</strong> <span style="color: %s; font-weight: bold;">%s</span></p>
                        <p><strong>Submitted:</strong> %s</p>
                    </div>
                    
                    <div style="background: %s; padding: 15px; border-radius: 8px; margin: 20px 0;">
                        <p style="margin: 0; color: %s; font-weight: bold;">%s</p>
                    </div>
                    
                    <p>If you have any questions or need assistance, please contact us:</p>
                    <ul>
                        <li>Phone: +91 9850748273</li>
                        <li>Email: admin@sairajtravels.com</li>
                    </ul>
                    
                    <p>Thank you for choosing Sairaj Travels!</p>
                    
                    <hr style="margin: 30px 0; border: none; border-top: 1px solid #e5e7eb;">
                    <p style="font-size: 12px; color: #6b7280;">
                        Sairaj Travels - Your Trusted Travel Partner<br>
                        This is an automated email. Please do not reply.
                    </p>
                </div>
            </body>
            </html>
            """, 
            enquiry.getFullName(),
            enquiry.getEnquiryId(),
            enquiry.getService() != null ? enquiry.getService() : "Not specified",
            enquiry.getMessage() != null ? enquiry.getMessage() : "Not provided",
            statusColor,
            enquiry.getStatus(),
            enquiry.getCreatedAt(),
            getEnquiryStatusBackgroundColor(enquiry.getStatus()),
            getEnquiryStatusTextColor(enquiry.getStatus()),
            statusMessage
        );
    }

    private String buildEnquiryUpdateEmailText(EnquiryDTO enquiry) {
        String statusMessage = getEnquiryStatusMessage(enquiry.getStatus());
        
        return String.format("""
            Enquiry Status Update
            
            Dear %s,
            
            Your enquiry status has been updated. Here are the current details:
            
            Enquiry Details:
            - Enquiry ID: #%d
            - Service: %s
            - Message: %s
            - Status: %s
            - Submitted: %s
            
            %s
            
            If you have any questions or need assistance, please contact us:
            - Phone: +91 9850748273
            - Email: admin@sairajtravels.com
            
            Thank you for choosing Sairaj Travels!
            
            ---
            Sairaj Travels - Your Trusted Travel Partner
            This is an automated email. Please do not reply.
            """, 
            enquiry.getFullName(),
            enquiry.getEnquiryId(),
            enquiry.getService() != null ? enquiry.getService() : "Not specified",
            enquiry.getMessage() != null ? enquiry.getMessage() : "Not provided",
            enquiry.getStatus(),
            enquiry.getCreatedAt(),
            statusMessage
        );
    }

    private String getEnquiryStatusColor(String status) {
        return switch (status.toUpperCase()) {
            case "RESOLVED" -> "#059669";
            case "CANCELLED" -> "#dc2626";
            case "PENDING" -> "#d97706";
            default -> "#6b7280";
        };
    }

    private String getEnquiryStatusBackgroundColor(String status) {
        return switch (status.toUpperCase()) {
            case "RESOLVED" -> "#d1fae5";
            case "CANCELLED" -> "#fee2e2";
            case "PENDING" -> "#fef3c7";
            default -> "#f3f4f6";
        };
    }

    private String getEnquiryStatusTextColor(String status) {
        return switch (status.toUpperCase()) {
            case "RESOLVED" -> "#065f46";
            case "CANCELLED" -> "#991b1b";
            case "PENDING" -> "#92400e";
            default -> "#374151";
        };
    }

    private String getEnquiryStatusMessage(String status) {
        return switch (status.toUpperCase()) {
            case "RESOLVED" -> "✅ Great news! Your enquiry has been resolved. We have addressed your request.";
            case "CANCELLED" -> "❌ Your enquiry has been cancelled. If you have any questions, please contact us.";
            case "PENDING" -> "⏳ Your enquiry is still being processed. We will update you soon.";
            default -> "Your enquiry status has been updated.";
        };
    }
}
