package com.parkpulse.dto;

public record PaymentKioskRequest(
        String facilityId,
        String kioskId,
        String eventType,
        String transactionId,
        String licensePlate
) {
}
