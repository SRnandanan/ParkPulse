package com.parkpulse.dto;

public record ParkingBayRequest(
        String facilityId,
        String bayId,
        String eventType,
        String licensePlate
) {
}
