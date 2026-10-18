package com.parkpulse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record GateSensorRequest(
        String facilityId,
        @NotBlank(message = "gateId is required")
        @Pattern(regexp = "^gate-[1-5]$", message = "gateId must be between gate-1 and gate-5")
        String gateId,
        String eventType,
        String licensePlate
) {
}
