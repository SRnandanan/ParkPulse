-- Extension for UUID support
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 1. Main Telemetry Events Table (Declarative Range Partitioning by Day)
CREATE TABLE IF NOT EXISTS telemetry_events (
    id UUID NOT NULL DEFAULT uuid_generate_v4(),
    event_type VARCHAR(50) NOT NULL,
    source_service VARCHAR(50) NOT NULL,
    facility_id VARCHAR(50) NOT NULL,
    event_timestamp TIMESTAMPTZ NOT NULL,
    payload JSONB NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id, event_timestamp)
) PARTITION BY RANGE (event_timestamp);

-- GIN Index on JSONB payload for efficient querying of arbitrary attributes
CREATE INDEX IF NOT EXISTS idx_telemetry_payload ON telemetry_events USING gin (payload);

-- Indexes for event properties
CREATE INDEX IF NOT EXISTS idx_telemetry_facility_type ON telemetry_events (facility_id, event_type);
CREATE INDEX IF NOT EXISTS idx_telemetry_timestamp ON telemetry_events (event_timestamp DESC);

-- Create Partitions for current dates and default partition
CREATE TABLE IF NOT EXISTS telemetry_events_y2026m10d14 PARTITION OF telemetry_events
    FOR VALUES FROM ('2026-10-14 00:00:00+00') TO ('2026-10-15 00:00:00+00');

CREATE TABLE IF NOT EXISTS telemetry_events_y2026m10d15 PARTITION OF telemetry_events
    FOR VALUES FROM ('2026-10-15 00:00:00+00') TO ('2026-10-16 00:00:00+00');

CREATE TABLE IF NOT EXISTS telemetry_events_y2026m10d16 PARTITION OF telemetry_events
    FOR VALUES FROM ('2026-10-16 00:00:00+00') TO ('2026-10-17 00:00:00+00');

CREATE TABLE IF NOT EXISTS telemetry_events_y2026m10d17 PARTITION OF telemetry_events
    FOR VALUES FROM ('2026-10-17 00:00:00+00') TO ('2026-10-18 00:00:00+00');

CREATE TABLE IF NOT EXISTS telemetry_events_default PARTITION OF telemetry_events DEFAULT;

-- 2. Active Parking Sessions Table
CREATE TABLE IF NOT EXISTS parking_sessions (
    session_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    license_plate VARCHAR(20) NOT NULL,
    facility_id VARCHAR(50) NOT NULL,
    entry_gate_id VARCHAR(50) NOT NULL,
    exit_gate_id VARCHAR(50),
    assigned_bay_id VARCHAR(50),
    entry_timestamp TIMESTAMPTZ NOT NULL,
    exit_timestamp TIMESTAMPTZ,
    payment_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    amount_paid NUMERIC(10, 2) NOT NULL DEFAULT 0.00,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_sessions_license_facility ON parking_sessions (license_plate, facility_id);
CREATE INDEX IF NOT EXISTS idx_sessions_payment_status ON parking_sessions (payment_status);

-- 3. Parking Bays Status Table
CREATE TABLE IF NOT EXISTS parking_bays (
    bay_id VARCHAR(50) NOT NULL,
    facility_id VARCHAR(50) NOT NULL,
    zone_id VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'VACANT',
    current_license_plate VARCHAR(20),
    last_status_change TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (facility_id, bay_id)
);

CREATE INDEX IF NOT EXISTS idx_bays_facility_zone ON parking_bays (facility_id, zone_id, status);
