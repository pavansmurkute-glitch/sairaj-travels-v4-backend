package com.sairajtravels.site.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class BookingRequestDTO {
    private Integer vehicleId;
    private String customerName;
    private String customerPhone;
    private LocalDate tripDate;
}
