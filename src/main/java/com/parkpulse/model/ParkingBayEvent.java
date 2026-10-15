package com.parkpulse.model;

import java.time.Instant;
import java.util.UUID;

public class ParkingBayEvent {

    private UUID id;

    private String eventType; // OCCUPIED, VACANT, SENSOR_STATUS

    private String sourceService; // parking-bay-service

    private String facilityId; // mall facility identifier

    private String bayId; // parking bay identifier

    private String licensePlate; // currently parked vehicle plate

    private Instant eventTimestamp;

    private String payload; // JSONB payload

    // Default constructor
    public ParkingBayEvent() {
    }

    // Constructor with fields
    public ParkingBayEvent(UUID id, String eventType, String sourceService,
                          String facilityId, String bayId, String licensePlate,
                          Instant eventTimestamp, String payload) {
        this.id = id;
        this.eventType = eventType;
        this.sourceService = sourceService;
        this.facilityId = facilityId;
        this.bayId = bayId;
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

    public String getBayId() {
        return bayId;
    }

    public void setBayId(String bayId) {
        this.bayId = bayId;
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