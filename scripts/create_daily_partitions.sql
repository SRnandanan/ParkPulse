DO $$
DECLARE
    partition_day date;
    partition_name text;
BEGIN
    FOR partition_day IN
        SELECT (CURRENT_TIMESTAMP AT TIME ZONE 'UTC')::date + day_offset
        FROM generate_series(0, 6) AS day_offset
    LOOP
        partition_name :=
            'telemetry_events_y' || to_char(partition_day, 'YYYY') ||
            'm' || to_char(partition_day, 'MM') ||
            'd' || to_char(partition_day, 'DD');

        EXECUTE format(
            'CREATE TABLE IF NOT EXISTS %I PARTITION OF telemetry_events
             FOR VALUES FROM (%L) TO (%L)',
            partition_name,
            partition_day::timestamp AT TIME ZONE 'UTC',
            (partition_day + 1)::timestamp AT TIME ZONE 'UTC'
        );
    END LOOP;
END
$$;