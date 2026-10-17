package com.parkpulse.generator;

import com.parkpulse.dto.GateSensorRequest;
import com.parkpulse.dto.ParkingBayRequest;
import com.parkpulse.dto.PaymentKioskRequest;
import com.parkpulse.model.GateSensorEvent;
import com.parkpulse.model.ParkingBayEvent;
import com.parkpulse.model.PaymentKioskEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static com.parkpulse.constants.ParkPulseConstants.BURST;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/telemetry")
public class TelemetryLoadController {

    private final TelemetryLoadGenerator loadGenerator;

    public TelemetryLoadController(TelemetryLoadGenerator loadGenerator) {
        this.loadGenerator = loadGenerator;
    }

    @PostMapping("/gate")
    public ResponseEntity<?> generateGateEvent(
        @RequestBody(required = false) GateSensorRequest request,
        @RequestParam (required = false) String mode
    ) {
        String facilityId = request != null && request.facilityId() != null ? request.facilityId() : "test-facility";
        String gateId = request != null && request.gateId() != null ? request.gateId() : "gate-1";
        if(BURST.equalsIgnoreCase(mode)) {
            // Generate a burst of events
            loadGenerator.generateGateEventBurst(facilityId, gateId, 10000);
            return ResponseEntity.status(202).body("Burst of gate events accepted for processing");
        }
        GateSensorEvent gateSensorEvent = loadGenerator.generateAndPublishGateEvent(facilityId, gateId);
        return ResponseEntity.status(200).body(gateSensorEvent);
    }

    @PostMapping("/bay")
    public ResponseEntity<?> generateBayEvent(
        @RequestBody(required = false) ParkingBayRequest request,
        @RequestParam (required = false) String mode
    ) {
        String facilityId = request != null && request.facilityId() != null ? request.facilityId() : "test-facility";
        String bayId = request != null && request.bayId() != null ? request.bayId() : "bay-1";
        if(BURST.equalsIgnoreCase(mode)) {
            // Generate a burst of events
            loadGenerator.generateBayEventBurst(facilityId, bayId, 10000);
            return ResponseEntity.status(202).body("Burst of bay events accepted for processing");
        }
        ParkingBayEvent parkingBayEvent = loadGenerator.generateAndPublishBayEvent(facilityId, bayId);
        return ResponseEntity.status(200).body(parkingBayEvent);
    }

    @PostMapping("/kiosk")
    public ResponseEntity<?> generateKioskEvent(
        @RequestBody(required = false) PaymentKioskRequest request,
        @RequestParam (required = false) String mode
    ) {
        String facilityId = request != null && request.facilityId() != null ? request.facilityId() : "test-facility";
        String kioskId = request != null && request.kioskId() != null ? request.kioskId() : "kiosk-1";
        if(BURST.equalsIgnoreCase(mode)) {
            // Generate a burst of events
            loadGenerator.generatePaymentEventBurst(facilityId, kioskId, 10000);
            return ResponseEntity.status(202).body("Burst of payment events accepted for processing");
        }
        PaymentKioskEvent paymentKioskEvent = loadGenerator.generateAndPublishPaymentEvent(facilityId, kioskId);
        return ResponseEntity.status(200).body(paymentKioskEvent);
    }
}