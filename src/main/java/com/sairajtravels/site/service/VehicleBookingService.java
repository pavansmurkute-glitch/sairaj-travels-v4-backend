package com.sairajtravels.site.service;

import com.sairajtravels.site.dto.VehicleBookingDTO;
import com.sairajtravels.site.entity.Vehicle;
import com.sairajtravels.site.entity.VehicleBooking;
import com.sairajtravels.site.repository.VehicleBookingRepository;
import com.sairajtravels.site.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleBookingService {

    private final VehicleBookingRepository vehicleBookingRepository;
    private final VehicleRepository vehicleRepository;
    private final EmailService emailService;

    public VehicleBookingService(VehicleBookingRepository vehicleBookingRepository,
                                 VehicleRepository vehicleRepository,
                                 EmailService emailService) {
        this.vehicleBookingRepository = vehicleBookingRepository;
        this.vehicleRepository = vehicleRepository;
        this.emailService = emailService;
    }

    private VehicleBookingDTO convertToDTO(VehicleBooking booking) {
        VehicleBookingDTO dto = new VehicleBookingDTO();
        dto.setBookingId(booking.getBookingId());
        dto.setVehicleId(booking.getVehicle() != null ? booking.getVehicle().getVehicleId() : null);
        dto.setPackageId(booking.getPackageId());
        dto.setCustomerName(booking.getCustomerName());
        dto.setCustomerPhone(booking.getCustomerPhone());
        dto.setCustomerEmail(booking.getCustomerEmail());
        dto.setPickupLocation(booking.getPickupLocation());
        dto.setDropLocation(booking.getDropLocation());
        dto.setTripDate(booking.getTripDate());
        dto.setReturnDate(booking.getReturnDate());
        dto.setTripTime(booking.getTripTime());
        dto.setPassengers(booking.getPassengers());
        dto.setLuggage(booking.getLuggage());
        dto.setSpecialRequests(booking.getSpecialRequests());
        dto.setStatus(booking.getStatus());
        dto.setRequestedAt(booking.getRequestedAt());
        return dto;
    }

    private VehicleBooking convertToEntity(VehicleBookingDTO dto) {
        VehicleBooking booking = new VehicleBooking();
        booking.setBookingId(dto.getBookingId());
        booking.setPackageId(dto.getPackageId());
        booking.setCustomerName(dto.getCustomerName());
        booking.setCustomerPhone(dto.getCustomerPhone());
        booking.setCustomerEmail(dto.getCustomerEmail());
        booking.setPickupLocation(dto.getPickupLocation());
        booking.setDropLocation(dto.getDropLocation());
        booking.setTripDate(dto.getTripDate());
        booking.setReturnDate(dto.getReturnDate());
        booking.setTripTime(dto.getTripTime());
        booking.setPassengers(dto.getPassengers());
        booking.setLuggage(dto.getLuggage());
        booking.setSpecialRequests(dto.getSpecialRequests());
        booking.setStatus(dto.getStatus());
        booking.setRequestedAt(dto.getRequestedAt());

        if (dto.getVehicleId() != null) {
            Optional<Vehicle> vehicle = vehicleRepository.findById(dto.getVehicleId());
            vehicle.ifPresent(booking::setVehicle);
        }

        return booking;
    }

    public List<VehicleBookingDTO> getAllBookings() {
        return vehicleBookingRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<VehicleBookingDTO> getBookingsByVehicle(Integer vehicleId) {
        return vehicleBookingRepository.findByVehicle_VehicleId(vehicleId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public VehicleBookingDTO createBooking(VehicleBookingDTO dto) {
        // Set default values
        if (dto.getStatus() == null || dto.getStatus().isEmpty()) {
            dto.setStatus("PENDING");
        }
        if (dto.getRequestedAt() == null) {
            dto.setRequestedAt(LocalDateTime.now());
        }

        VehicleBooking saved = vehicleBookingRepository.save(convertToEntity(dto));
        VehicleBookingDTO result = convertToDTO(saved);

        // Send email notifications
        sendBookingNotifications(result);

        return result;
    }

    public VehicleBookingDTO updateBooking(Integer id, VehicleBookingDTO dto) {
        Optional<VehicleBooking> existingBookingOpt = vehicleBookingRepository.findById(id);
        if (existingBookingOpt.isPresent()) {
            VehicleBooking existingBooking = existingBookingOpt.get();
            
            // Store original status for comparison
            String originalStatus = existingBooking.getStatus();
            
            // Update only the fields that are provided in the DTO
            if (dto.getStatus() != null) {
                existingBooking.setStatus(dto.getStatus());
            }
            if (dto.getCustomerName() != null) {
                existingBooking.setCustomerName(dto.getCustomerName());
            }
            if (dto.getCustomerPhone() != null) {
                existingBooking.setCustomerPhone(dto.getCustomerPhone());
            }
            if (dto.getCustomerEmail() != null) {
                existingBooking.setCustomerEmail(dto.getCustomerEmail());
            }
            if (dto.getPickupLocation() != null) {
                existingBooking.setPickupLocation(dto.getPickupLocation());
            }
            if (dto.getDropLocation() != null) {
                existingBooking.setDropLocation(dto.getDropLocation());
            }
            if (dto.getTripDate() != null) {
                existingBooking.setTripDate(dto.getTripDate());
            }
            if (dto.getReturnDate() != null) {
                existingBooking.setReturnDate(dto.getReturnDate());
            }
            if (dto.getTripTime() != null) {
                existingBooking.setTripTime(dto.getTripTime());
            }
            if (dto.getPassengers() != null) {
                existingBooking.setPassengers(dto.getPassengers());
            }
            if (dto.getLuggage() != null) {
                existingBooking.setLuggage(dto.getLuggage());
            }
            if (dto.getSpecialRequests() != null) {
                existingBooking.setSpecialRequests(dto.getSpecialRequests());
            }
            if (dto.getVehicleId() != null) {
                Optional<Vehicle> vehicle = vehicleRepository.findById(dto.getVehicleId());
                vehicle.ifPresent(existingBooking::setVehicle);
            }
            if (dto.getPackageId() != null) {
                existingBooking.setPackageId(dto.getPackageId());
            }
            
            VehicleBooking updated = vehicleBookingRepository.save(existingBooking);
            VehicleBookingDTO result = convertToDTO(updated);
            
            // Debug logging
            System.out.println("=== BOOKING UPDATE DEBUG ===");
            System.out.println("Booking ID: " + id);
            System.out.println("DTO Status: " + dto.getStatus());
            System.out.println("Original Status: " + originalStatus);
            System.out.println("New Status: " + existingBooking.getStatus());
            System.out.println("Status changed: " + (dto.getStatus() != null && !dto.getStatus().equals(originalStatus)));
            
            // Send email notification for status changes
            if (dto.getStatus() != null && !dto.getStatus().equals(originalStatus)) {
                System.out.println("Status changed - sending email notification");
                sendBookingUpdateNotification(result);
            } else {
                System.out.println("No status change - skipping email notification");
            }
            
            return result;
        }
        return null;
    }

    public void deleteBooking(Integer id) {
        vehicleBookingRepository.deleteById(id);
    }

    private void sendBookingNotifications(VehicleBookingDTO booking) {
        try {
            System.out.println("=== SENDING BOOKING NOTIFICATIONS ===");
            System.out.println("Booking ID: " + booking.getBookingId());
            System.out.println("Customer: " + booking.getCustomerName());
            System.out.println("Email: " + booking.getCustomerEmail());
            
            // Get vehicle details
            String vehicleName = "Unknown Vehicle";
            if (booking.getVehicleId() != null) {
                Optional<Vehicle> vehicle = vehicleRepository.findById(booking.getVehicleId());
                if (vehicle.isPresent()) {
                    vehicleName = vehicle.get().getName();
                }
            }

            // Send email to customer (don't fail if this fails)
            if (booking.getCustomerEmail() != null && !booking.getCustomerEmail().isEmpty()) {
                try {
                    String customerSubject = "Booking Confirmation - Sairaj Travels";
                    String customerHtml = buildCustomerEmailHtml(booking, vehicleName);
                    String customerText = buildCustomerEmailText(booking, vehicleName);
                    emailService.sendHtmlEmail(booking.getCustomerEmail(), customerSubject, customerHtml, customerText);
                    System.out.println("✅ Customer email sent successfully");
                } catch (Exception emailError) {
                    System.err.println("⚠️ Failed to send customer email (booking still saved): " + emailError.getMessage());
                    emailError.printStackTrace();
                }
            } else {
                System.out.println("ℹ️ No customer email provided - skipping customer notification");
            }

            // Send email to admin (don't fail if this fails)
            try {
                String adminSubject = "New Booking Request - " + booking.getCustomerName();
                String adminHtml = buildAdminEmailHtml(booking, vehicleName);
                String adminText = buildAdminEmailText(booking, vehicleName);
                emailService.notifyAdmin(adminSubject, adminHtml, adminText);
                System.out.println("✅ Admin email sent successfully");
            } catch (Exception adminEmailError) {
                System.err.println("⚠️ Failed to send admin email (booking still saved): " + adminEmailError.getMessage());
                adminEmailError.printStackTrace();
            }

            System.out.println("=== BOOKING NOTIFICATIONS COMPLETED ===");

        } catch (Exception e) {
            System.err.println("❌ Critical error in booking notifications: " + e.getMessage());
            e.printStackTrace();
            // Don't rethrow - booking should still be saved even if emails fail
        }
    }

    private String buildCustomerEmailHtml(VehicleBookingDTO booking, String vehicleName) {
        return String.format("""
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                    <h2 style="color: #2563eb;">Thank you for your booking with Sairaj Travels!</h2>
                    
                    <p>Dear %s,</p>
                    
                    <p>We have received your booking request and will process it shortly. Here are your booking details:</p>
                    
                    <div style="background: #f8fafc; padding: 20px; border-radius: 8px; margin: 20px 0;">
                        <h3 style="color: #1e40af; margin-top: 0;">Booking Details</h3>
                        <p><strong>Booking ID:</strong> #%d</p>
                        <p><strong>Vehicle:</strong> %s</p>
                        <p><strong>Pickup Location:</strong> %s</p>
                        <p><strong>Drop Location:</strong> %s</p>
                        <p><strong>Trip Date:</strong> %s</p>
                        %s
                        <p><strong>Passengers:</strong> %d</p>
                        <p><strong>Status:</strong> %s</p>
                    </div>
                    
                    <p>Our team will contact you within 24 hours to confirm your booking and provide further details.</p>
                    
                    <p>For any queries, please contact us at:</p>
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
            booking.getCustomerName(),
            booking.getBookingId(),
            vehicleName,
            booking.getPickupLocation() != null ? booking.getPickupLocation() : "Not specified",
            booking.getDropLocation() != null ? booking.getDropLocation() : "Not specified",
            booking.getTripDate(),
            booking.getReturnDate() != null ? String.format("<p><strong>Return Date:</strong> %s</p>", booking.getReturnDate()) : "",
            booking.getPassengers() != null ? booking.getPassengers() : 1,
            booking.getStatus()
        );
    }

    private String buildCustomerEmailText(VehicleBookingDTO booking, String vehicleName) {
        return String.format("""
            Thank you for your booking with Sairaj Travels!
            
            Dear %s,
            
            We have received your booking request and will process it shortly. Here are your booking details:
            
            Booking Details:
            - Booking ID: #%d
            - Vehicle: %s
            - Pickup Location: %s
            - Drop Location: %s
            - Trip Date: %s
            %s
            - Passengers: %d
            - Status: %s
            
            Our team will contact you within 24 hours to confirm your booking and provide further details.
            
            For any queries, please contact us at:
            - Phone: +91 9850748273
            - Email: admin@sairajtravels.com
            
            Thank you for choosing Sairaj Travels!
            
            ---
            Sairaj Travels - Your Trusted Travel Partner
            This is an automated email. Please do not reply.
            """, 
            booking.getCustomerName(),
            booking.getBookingId(),
            vehicleName,
            booking.getPickupLocation() != null ? booking.getPickupLocation() : "Not specified",
            booking.getDropLocation() != null ? booking.getDropLocation() : "Not specified",
            booking.getTripDate(),
            booking.getReturnDate() != null ? String.format("- Return Date: %s", booking.getReturnDate()) : "",
            booking.getPassengers() != null ? booking.getPassengers() : 1,
            booking.getStatus()
        );
    }

    private String buildAdminEmailHtml(VehicleBookingDTO booking, String vehicleName) {
        return String.format("""
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                    <h2 style="color: #dc2626;">New Booking Request</h2>
                    
                    <p>A new booking has been submitted through the website:</p>
                    
                    <div style="background: #fef2f2; padding: 20px; border-radius: 8px; margin: 20px 0; border-left: 4px solid #dc2626;">
                        <h3 style="color: #dc2626; margin-top: 0;">Booking Details</h3>
                        <p><strong>Booking ID:</strong> #%d</p>
                        <p><strong>Customer Name:</strong> %s</p>
                        <p><strong>Phone:</strong> %s</p>
                        <p><strong>Email:</strong> %s</p>
                        <p><strong>Vehicle:</strong> %s</p>
                        <p><strong>Pickup Location:</strong> %s</p>
                        <p><strong>Drop Location:</strong> %s</p>
                        <p><strong>Trip Date:</strong> %s</p>
                        %s
                        <p><strong>Passengers:</strong> %d</p>
                        <p><strong>Luggage:</strong> %s</p>
                        <p><strong>Special Requests:</strong> %s</p>
                        <p><strong>Status:</strong> %s</p>
                        <p><strong>Requested At:</strong> %s</p>
                    </div>
                    
                    <p style="color: #dc2626; font-weight: bold;">Action Required: Please contact the customer to confirm the booking.</p>
                </div>
            </body>
            </html>
            """, 
            booking.getBookingId(),
            booking.getCustomerName(),
            booking.getCustomerPhone(),
            booking.getCustomerEmail() != null ? booking.getCustomerEmail() : "Not provided",
            vehicleName,
            booking.getPickupLocation() != null ? booking.getPickupLocation() : "Not specified",
            booking.getDropLocation() != null ? booking.getDropLocation() : "Not specified",
            booking.getTripDate(),
            booking.getReturnDate() != null ? String.format("<p><strong>Return Date:</strong> %s</p>", booking.getReturnDate()) : "",
            booking.getPassengers() != null ? booking.getPassengers() : 1,
            booking.getLuggage() != null ? booking.getLuggage() : "Not specified",
            booking.getSpecialRequests() != null ? booking.getSpecialRequests() : "None",
            booking.getStatus(),
            booking.getRequestedAt()
        );
    }

    private String buildAdminEmailText(VehicleBookingDTO booking, String vehicleName) {
        return String.format("""
            New Booking Request
            
            A new booking has been submitted through the website:
            
            Booking Details:
            - Booking ID: #%d
            - Customer Name: %s
            - Phone: %s
            - Email: %s
            - Vehicle: %s
            - Pickup Location: %s
            - Drop Location: %s
            - Trip Date: %s
            %s
            - Passengers: %d
            - Luggage: %s
            - Special Requests: %s
            - Status: %s
            - Requested At: %s
            
            Action Required: Please contact the customer to confirm the booking.
            """, 
            booking.getBookingId(),
            booking.getCustomerName(),
            booking.getCustomerPhone(),
            booking.getCustomerEmail() != null ? booking.getCustomerEmail() : "Not provided",
            vehicleName,
            booking.getPickupLocation() != null ? booking.getPickupLocation() : "Not specified",
            booking.getDropLocation() != null ? booking.getDropLocation() : "Not specified",
            booking.getTripDate(),
            booking.getReturnDate() != null ? String.format("- Return Date: %s", booking.getReturnDate()) : "",
            booking.getPassengers() != null ? booking.getPassengers() : 1,
            booking.getLuggage() != null ? booking.getLuggage() : "Not specified",
            booking.getSpecialRequests() != null ? booking.getSpecialRequests() : "None",
            booking.getStatus(),
            booking.getRequestedAt()
        );
    }

    private void sendBookingUpdateNotification(VehicleBookingDTO booking) {
        try {
            System.out.println("=== SENDING BOOKING UPDATE NOTIFICATION ===");
            System.out.println("Booking ID: " + booking.getBookingId());
            System.out.println("Customer Email: " + booking.getCustomerEmail());
            System.out.println("Status: " + booking.getStatus());
            
            // Get vehicle details
            String vehicleName = "Unknown Vehicle";
            if (booking.getVehicleId() != null) {
                Optional<Vehicle> vehicle = vehicleRepository.findById(booking.getVehicleId());
                if (vehicle.isPresent()) {
                    vehicleName = vehicle.get().getName();
                }
            }

            // Send email to customer about booking status change (don't fail if this fails)
            if (booking.getCustomerEmail() != null && !booking.getCustomerEmail().isEmpty()) {
                try {
                    System.out.println("Sending email to: " + booking.getCustomerEmail());
                    String customerSubject = "Booking Status Update - Sairaj Travels";
                    String customerHtml = buildBookingUpdateEmailHtml(booking, vehicleName);
                    String customerText = buildBookingUpdateEmailText(booking, vehicleName);
                    emailService.sendHtmlEmail(booking.getCustomerEmail(), customerSubject, customerHtml, customerText);
                    System.out.println("✅ Email notification sent for booking: " + booking.getBookingId());
                } catch (Exception emailError) {
                    System.err.println("⚠️ Failed to send booking update email (update still saved): " + emailError.getMessage());
                    emailError.printStackTrace();
                }
            } else {
                System.out.println("ℹ️ No customer email found for booking: " + booking.getBookingId());
            }

            System.out.println("=== BOOKING UPDATE NOTIFICATION COMPLETED ===");

        } catch (Exception e) {
            System.err.println("❌ Critical error in booking update notification: " + e.getMessage());
            e.printStackTrace();
            // Don't rethrow - booking update should still be saved even if emails fail
        }
    }

    private String buildBookingUpdateEmailHtml(VehicleBookingDTO booking, String vehicleName) {
        String statusColor = getStatusColor(booking.getStatus());
        String statusMessage = getStatusMessage(booking.getStatus());
        
        return String.format("""
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                    <h2 style="color: #2563eb;">Booking Status Update</h2>
                    
                    <p>Dear %s,</p>
                    
                    <p>Your booking status has been updated. Here are the current details:</p>
                    
                    <div style="background: #f8fafc; padding: 20px; border-radius: 8px; margin: 20px 0;">
                        <h3 style="color: #1e40af; margin-top: 0;">Booking Details</h3>
                        <p><strong>Booking ID:</strong> #%d</p>
                        <p><strong>Vehicle:</strong> %s</p>
                        <p><strong>Pickup Location:</strong> %s</p>
                        <p><strong>Drop Location:</strong> %s</p>
                        <p><strong>Trip Date:</strong> %s</p>
                        %s
                        <p><strong>Passengers:</strong> %d</p>
                        <p><strong>Status:</strong> <span style="color: %s; font-weight: bold;">%s</span></p>
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
            booking.getCustomerName(),
            booking.getBookingId(),
            vehicleName,
            booking.getPickupLocation() != null ? booking.getPickupLocation() : "Not specified",
            booking.getDropLocation() != null ? booking.getDropLocation() : "Not specified",
            booking.getTripDate(),
            booking.getReturnDate() != null ? String.format("<p><strong>Return Date:</strong> %s</p>", booking.getReturnDate()) : "",
            booking.getPassengers() != null ? booking.getPassengers() : 1,
            statusColor,
            booking.getStatus(),
            getStatusBackgroundColor(booking.getStatus()),
            getStatusTextColor(booking.getStatus()),
            statusMessage
        );
    }

    private String buildBookingUpdateEmailText(VehicleBookingDTO booking, String vehicleName) {
        String statusMessage = getStatusMessage(booking.getStatus());
        
        return String.format("""
            Booking Status Update
            
            Dear %s,
            
            Your booking status has been updated. Here are the current details:
            
            Booking Details:
            - Booking ID: #%d
            - Vehicle: %s
            - Pickup Location: %s
            - Drop Location: %s
            - Trip Date: %s
            %s
            - Passengers: %d
            - Status: %s
            
            %s
            
            If you have any questions or need assistance, please contact us:
            - Phone: +91 9850748273
            - Email: admin@sairajtravels.com
            
            Thank you for choosing Sairaj Travels!
            
            ---
            Sairaj Travels - Your Trusted Travel Partner
            This is an automated email. Please do not reply.
            """, 
            booking.getCustomerName(),
            booking.getBookingId(),
            vehicleName,
            booking.getPickupLocation() != null ? booking.getPickupLocation() : "Not specified",
            booking.getDropLocation() != null ? booking.getDropLocation() : "Not specified",
            booking.getTripDate(),
            booking.getReturnDate() != null ? String.format("- Return Date: %s", booking.getReturnDate()) : "",
            booking.getPassengers() != null ? booking.getPassengers() : 1,
            booking.getStatus(),
            statusMessage
        );
    }

    private String getStatusColor(String status) {
        return switch (status.toUpperCase()) {
            case "CONFIRMED" -> "#059669";
            case "CANCELLED" -> "#dc2626";
            case "PENDING" -> "#d97706";
            default -> "#6b7280";
        };
    }

    private String getStatusBackgroundColor(String status) {
        return switch (status.toUpperCase()) {
            case "CONFIRMED" -> "#d1fae5";
            case "CANCELLED" -> "#fee2e2";
            case "PENDING" -> "#fef3c7";
            default -> "#f3f4f6";
        };
    }

    private String getStatusTextColor(String status) {
        return switch (status.toUpperCase()) {
            case "CONFIRMED" -> "#065f46";
            case "CANCELLED" -> "#991b1b";
            case "PENDING" -> "#92400e";
            default -> "#374151";
        };
    }

    private String getStatusMessage(String status) {
        return switch (status.toUpperCase()) {
            case "CONFIRMED" -> "🎉 Great news! Your booking has been confirmed. We will contact you soon with further details.";
            case "CANCELLED" -> "❌ Your booking has been cancelled. If you have any questions, please contact us.";
            case "PENDING" -> "⏳ Your booking is still being processed. We will update you soon.";
            default -> "Your booking status has been updated.";
        };
    }
}
