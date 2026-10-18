package com.parkpulse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PaymentKioskRequest(
        String facilityId,
        @NotBlank(message = "kioskId is required")
        @Pattern(regexp = "^kiosk-[1-5]$", message = "kioskId must be between kiosk-1 and kiosk-5")
        String kioskId,
        String eventType,
        String transactionId,
        String licensePlate
) {
}
