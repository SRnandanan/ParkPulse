package com.parkpulse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ParkingBayRequest(
        String facilityId,
        @NotBlank(message = "bayId is required")
        @Pattern(regexp = "^bay-(?:[1-9]|1[0-9])$", message = "bayId must be between bay-1 and bay-19")
        String bayId,
        String eventType,
        String licensePlate
) {
}
