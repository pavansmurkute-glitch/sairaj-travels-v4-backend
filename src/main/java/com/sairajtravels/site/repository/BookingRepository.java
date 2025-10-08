package com.sairajtravels.site.repository;

import com.sairajtravels.site.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    
    // Find bookings by user
    @Query("SELECT b FROM Booking b WHERE b.user.userId = :userId")
    List<Booking> findByUserId(@Param("userId") Integer userId);
    
    // Find bookings by date range
    @Query("SELECT b FROM Booking b WHERE b.journeyDate BETWEEN :startDate AND :endDate")
    List<Booking> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    // Find bookings by status
    List<Booking> findByBookingStatus(String status);
    
    // Find bookings by bus
    @Query("SELECT b FROM Booking b WHERE b.bus.busId = :busId")
    List<Booking> findByBusId(@Param("busId") Integer busId);
    
    // Count bookings by status
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.bookingStatus = :status")
    Long countByBookingStatus(@Param("status") String status);
    
    // Find recent bookings (last 30 days)
    @Query("SELECT b FROM Booking b WHERE b.createdAt >= :since ORDER BY b.createdAt DESC")
    List<Booking> findRecentBookings(@Param("since") java.time.LocalDateTime since);
}