package com.parkpulse.dto;

public record GateSensorRequest(
        String facilityId,
        String gateId,
        String eventType,
        String licensePlate
) {
}
