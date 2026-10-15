package com.parkpulse.model;

import java.time.Instant;
import java.util.UUID;

public class PaymentKioskEvent {

    private UUID id;

    private String eventType; // PAYMENT, VALIDATION, REFUND

    private String sourceService; // payment-kiosk-service

    private String facilityId; // mall facility identifier

    private String kioskId; // payment kiosk identifier

    private String transactionId; // unique transaction identifier

    private String licensePlate; // vehicle license plate

    private Instant eventTimestamp;

    private String payload; // JSONB payload

    // Default constructor
    public PaymentKioskEvent() {
    }

    // Constructor with fields
    public PaymentKioskEvent(UUID id, String eventType, String sourceService,
                             String facilityId, String kioskId, String transactionId,
                             String licensePlate, Instant eventTimestamp, String payload) {
        this.id = id;
        this.eventType = eventType;
        this.sourceService = sourceService;
        this.facilityId = facilityId;
        this.kioskId = kioskId;
        this.transactionId = transactionId;
        this.licensePlate = licensePlate;
        this.eventTimestamp = eventTimestamp;
        this.payload = payload;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getSourceService() {
        return sourceService;
    }

    public void setSourceService(String sourceService) {
        this.sourceService = sourceService;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getKioskId() {
        return kioskId;
    }

    public void setKioskId(String kioskId) {
        this.kioskId = kioskId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Instant getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(Instant eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}