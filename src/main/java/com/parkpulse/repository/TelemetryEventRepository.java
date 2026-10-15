package com.parkpulse.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Repository
public class TelemetryEventRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public TelemetryEventRepository(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    public void save(UUID id, String eventType, String sourceService, String facilityId,
                     Instant eventTimestamp, Object event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            jdbcTemplate.update(
                    "INSERT INTO telemetry_events " +
                            "(id, event_type, source_service, facility_id, event_timestamp, payload) " +
                            "VALUES (?, ?, ?, ?, ?, CAST(? AS jsonb))",
                    id, eventType, sourceService, facilityId, Timestamp.from(eventTimestamp), payload);
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Unable to serialize telemetry event", exception);
        }
    }
}