package com.parkpulse.generator;

import com.parkpulse.model.GateSensorEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.parkpulse.model.ParkingBayEvent;
import com.parkpulse.model.PaymentKioskEvent;


@RestController
@RequestMapping("/api/telemetry/generate")
public class TelemetryLoadController {

    private final TelemetryLoadGenerator loadGenerator;

    public TelemetryLoadController(TelemetryLoadGenerator loadGenerator) {
        this.loadGenerator = loadGenerator;
    }

    @PostMapping("/gate")
    public GateSensorEvent generateGateEvent(
            @RequestParam(defaultValue = "test-facility") String facilityId,
            @RequestParam(defaultValue = "gate-1") String gateId) {
        return loadGenerator.generateAndPublishGateEvent(facilityId, gateId);
    }

    @PostMapping ("/bay")
    public ParkingBayEvent generateBayEvent(
            @RequestParam(defaultValue = "test-facility") String facilityId,
            @RequestParam(defaultValue = "bay-1") String bayId) {
        return loadGenerator.generateAndPublishBayEvent(facilityId, bayId);
    }

    @PostMapping ("/kiosk")
    public PaymentKioskEvent generateKioskEvent(
            @RequestParam(defaultValue = "test-facility") String facilityId,
            @RequestParam(defaultValue = "kiosk-1") String kioskId) {
        return loadGenerator.generateAndPublishPaymentEvent(facilityId, kioskId);
    }
}